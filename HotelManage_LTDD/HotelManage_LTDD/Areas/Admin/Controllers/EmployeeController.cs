using Firebase.Storage;
using FireSharp.Interfaces;
using FireSharp.Response;
using HotelManage_LTDD.Models;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;

namespace HotelManage_LTDD.Areas.Admin.Controllers
{
    [Area("Admin")]
    public class EmployeeController : Controller
    {
        private readonly IFirebaseClient _client;

        public EmployeeController(IFirebaseClient client)
        {
            _client = client;
        }
        [Route("/Admin/Employee")]
        public ActionResult Index()
        {
            return View();
        }

        [Route("/Admin/Employee/Create")]
        public ActionResult Create()
        {
            return View();
        }

        [Route("/Admin/Employee/List")]
        public async Task<IActionResult> getList()
        {
            FirebaseResponse response = _client.Get("Users");
            // Deserialize JSON từ Firebase response
            var users = JsonConvert.DeserializeObject<Dictionary<string, User>>(response.Body);

            // Lọc user có Role là "Employee"
            var employeeUsers = users
                .Where(u => u.Value.Role == "Employee")
                .Select(u => u.Value)
                .ToList();

            // Trả về kết quả
            return Json(employeeUsers);
        }

        [Route("/Admin/Employee/Details/{id}")]
        public ActionResult Details(string id)
        {
            // Lấy dữ liệu từ Firebase theo ID
            FirebaseResponse response = _client.Get("Users/" + id);
            dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

            if (data == null)
            {
                return NotFound();

            }
            var user = JsonConvert.DeserializeObject<User>(JsonConvert.SerializeObject(data));
            return View(user);
        }

        [Route("/Admin/Employee/Create")]
        // POST: RoomTypeController/Create
        [HttpPost]
        public async Task<ActionResult> Create(User user, IFormFile AvatarUrl)
        {
            if (ModelState.IsValid)
            {
                if (AvatarUrl != null && AvatarUrl.Length > 0)
                {
                    // Kiểm tra loại file (ví dụ chỉ cho phép file hình ảnh)
                    var allowedExtensions = new[] { ".jpg", ".jpeg", ".png" };
                    var extension = Path.GetExtension(AvatarUrl.FileName).ToLower();
                    if (!allowedExtensions.Contains(extension))
                    {
                        ModelState.AddModelError(string.Empty, "Only image files (.jpg, .jpeg, .png) are allowed.");
                        return View();
                    }

                    // Đẩy ảnh lên Firebase Storage và lấy URL của ảnh
                    var imageUrl = await UploadImageToFirebaseStorage(AvatarUrl);
                    user.AvatarUrl = imageUrl;  // Gán URL ảnh vào thuộc tính của RoomType
                }

                var data = user;
                PushResponse response = _client.Push("Users/", data);
                data.Id = response.Result.name;
                SetResponse setResponse = _client.Set("Users/" + data.Id, data);

                if (setResponse.StatusCode == System.Net.HttpStatusCode.OK)
                {
                    ModelState.AddModelError(string.Empty, "Added Successfully");
                    return RedirectToAction("Index");
                }
                else
                {
                    ModelState.AddModelError(string.Empty, "Something went wrong!!");
                }
            }

            return View();
        }

        [Route("/Admin/Employee/Edit/{id}")]
        // GET: RoomTypeController/Edit/5
        public ActionResult Edit(string id)
        {
            FirebaseResponse response = _client.Get("Users/" + id);
            dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

            if (data == null)
            {
                return NotFound();
            }

            // Chuyển dữ liệu thành đối tượng RoomType
            var user = JsonConvert.DeserializeObject<User>(JsonConvert.SerializeObject(data));
            return View(user);
        }
        [Route("/Admin/Employee/Edit/{id}")]
        [HttpPost]
        public async Task<ActionResult> Edit(string id, User user, IFormFile AvatarUrl = null)
        {
            if (ModelState.IsValid)
            {

                FirebaseResponse response = _client.Get("Users/" + id);
                dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

                if (data == null)
                {
                    return NotFound();
                }

                // Chuyển dữ liệu thành đối tượng RoomType
                var curentUser = JsonConvert.DeserializeObject<User>(JsonConvert.SerializeObject(data));
                var currentImage = curentUser.AvatarUrl;

                // Kiểm tra nếu có ảnh mới được chọn
                if (AvatarUrl != null && AvatarUrl.Length > 0)
                {
                    // Kiểm tra loại file (chỉ cho phép ảnh)
                    var allowedExtensions = new[] { ".jpg", ".jpeg", ".png" };
                    var extension = Path.GetExtension(AvatarUrl.FileName).ToLower();
                    if (!allowedExtensions.Contains(extension))
                    {
                        ModelState.AddModelError(string.Empty, "Only image files (.jpg, .jpeg, .png) are allowed.");
                        return View(user);
                    }


                    if (currentImage != null)
                    {
                        await DeleteImageFromFirebaseStorage(currentImage);
                    }


                    // Đẩy ảnh mới lên Firebase Storage và lấy URL
                    var newImageUrl = await UploadImageToFirebaseStorage(AvatarUrl);
                    user.AvatarUrl = newImageUrl;  // Gán lại ảnh mới
                }
                else
                {
                    user.AvatarUrl = currentImage;
                }

                // Cập nhật lại thông tin loại phòng trong Firebase
                response = _client.Set("Users/" + id, user);

                if (response.StatusCode == System.Net.HttpStatusCode.OK)
                {
                    ModelState.AddModelError(string.Empty, "Updated Successfully");
                    return RedirectToAction("Index");
                }
                else
                {
                    ModelState.AddModelError(string.Empty, "Something went wrong!!");
                }
            }

            return View(user);
        }

        [Route("/Admin/Employee/Delete/{id}")]
        [HttpPost]
        public async Task<ActionResult> Delete(string id)
        {
            try
            {
                FirebaseResponse response = _client.Get("Users/" + id);
                dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

                if (data == null)
                {
                    return Json(new { error = true, message = "Không tìm thấy người dùng!" });
                }

                // Chuyển dữ liệu thành đối tượng RoomType
                var curentUser = JsonConvert.DeserializeObject<User>(JsonConvert.SerializeObject(data));
                var currentImage = curentUser.AvatarUrl;

                if (currentImage != null)
                {
                    await DeleteImageFromFirebaseStorage(currentImage);
                }


                // Xóa loại phòng khỏi Firebase
                response = _client.Delete("Users/" + id);

                if (response.StatusCode == System.Net.HttpStatusCode.OK)
                {
                    return Json(new { success = true, message = "Xóa thành công!" });
                }
                else
                {
                    return Json(new { error = true, message = "Có lỗi khi xóa!" });
                }
            }
            catch
            {
                return Json(new { error = true, message = "Có lỗi xảy ra!" });
            }
        }

        public async Task<string> UploadImageToFirebaseStorage(IFormFile file)
        {
            try
            {
                // Thiết lập các thông số Firebase Storage
                var storage = new FirebaseStorage("ltddhaha.firebasestorage.app");

                // Tạo tên file duy nhất để tránh bị ghi đè
                var fileName = Guid.NewGuid().ToString() + Path.GetExtension(file.FileName);

                // Tải lên file
                var task = storage.Child("UserImages") // Lưu ảnh vào thư mục "RoomImages"
                                  .Child(fileName)
                                  .PutAsync(file.OpenReadStream());

                // Đợi quá trình tải lên hoàn tất
                await task;

                // Lấy URL tải được của ảnh
                var imageUrl = await storage.Child("UserImages")
                                            .Child(fileName)
                                            .GetDownloadUrlAsync();

                return imageUrl;
            }
            catch (Exception ex)
            {
                // Xử lý lỗi khi tải ảnh lên Firebase
                throw new Exception("Error uploading image: " + ex.Message);
            }
        }
        public async Task DeleteImageFromFirebaseStorage(string imageUrl)
        {
            try
            {
                string url = imageUrl;

                // Tìm vị trí của '%2F' và '?'
                int startIndex = url.IndexOf("%2F") + 3;  // Tính từ vị trí sau "%2F"
                int endIndex = url.IndexOf("?", startIndex); // Tìm dấu hỏi chấm bắt đầu từ startIndex

                // Trích xuất tên ảnh
                string fileName = url.Substring(startIndex, endIndex - startIndex);

                // Tạo instance FirebaseStorage và xóa ảnh
                var storage = new FirebaseStorage("ltddhaha.firebasestorage.app");
                await storage.Child("UserImages").Child(fileName).DeleteAsync();
            }
            catch (Exception ex)
            {
                // Xử lý lỗi nếu không thể xóa ảnh
                throw new Exception("Error deleting image from Firebase: " + ex.Message);
            }
        }
    }
}
