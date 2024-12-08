using FireSharp.Interfaces;
using FireSharp.Response;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using HotelManage_LTDD.Models;
using Firebase.Storage;
using System.Linq;

namespace HotelManage_LTDD.Controllers
{
    [Area("Admin")]
    public class RoomTypeController : Controller
    {

        private readonly IFirebaseClient _client;

        public RoomTypeController(IFirebaseClient client)
        {
            _client = client;
        }
        [Route("/Admin")]
        public ActionResult Index()
        {
            return View();
        }

        [Route("/Admin/RoomType/Create")]
        public ActionResult Create()
        {
            return View();
        }

        [Route("/Admin/RoomType/List")]
        public async Task<IActionResult> getList()
        {
            FirebaseResponse response = _client.Get("RoomTypes");
            return Content(response.Body, "application/json");
        }

        [Route("/Admin/RoomType/Details/{id}")]
        public ActionResult Details(string id)
        {
            // Lấy dữ liệu từ Firebase theo ID
            FirebaseResponse response = _client.Get("RoomTypes/" + id);
            dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

            if (data == null)
            {
                return NotFound();
               
            }
            var roomType = JsonConvert.DeserializeObject<RoomType>(JsonConvert.SerializeObject(data));
            return View(roomType);
        }

        [Route("/Admin/RoomType/Create")]
        // POST: RoomTypeController/Create
        [HttpPost]
        public async Task<ActionResult> Create(RoomType RoomType, IFormFile ImageUrl)
        {
            if (ModelState.IsValid)
            {
                if (ImageUrl != null && ImageUrl.Length > 0)
                {
                    // Kiểm tra loại file (ví dụ chỉ cho phép file hình ảnh)
                    var allowedExtensions = new[] { ".jpg", ".jpeg", ".png" };
                    var extension = Path.GetExtension(ImageUrl.FileName).ToLower();
                    if (!allowedExtensions.Contains(extension))
                    {
                        ModelState.AddModelError(string.Empty, "Only image files (.jpg, .jpeg, .png) are allowed.");
                        return View();
                    }

                    // Đẩy ảnh lên Firebase Storage và lấy URL của ảnh
                    var imageUrl = await UploadImageToFirebaseStorage(ImageUrl);
                    RoomType.ImageUrl = imageUrl;  // Gán URL ảnh vào thuộc tính của RoomType
                }

                var data = RoomType;
                PushResponse response = _client.Push("RoomTypes/", data);
                data.Id = response.Result.name;
                SetResponse setResponse = _client.Set("RoomTypes/" + data.Id, data);

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

        [Route("/Admin/RoomType/Edit/{id}")]
        // GET: RoomTypeController/Edit/5
        public ActionResult Edit(string id)
        {
            FirebaseResponse response = _client.Get("RoomTypes/" + id);
            dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

            if (data == null)
            {
                return NotFound();
            }

            // Chuyển dữ liệu thành đối tượng RoomType
            var roomType = JsonConvert.DeserializeObject<RoomType>(JsonConvert.SerializeObject(data));
            return View(roomType);
        }
        [Route("/Admin/RoomType/Edit/{id}")]
        [HttpPost]
        public async Task<ActionResult> Edit(string id, RoomType roomType, IFormFile ImageUrl = null)
        {
            if (ModelState.IsValid)
            {

                FirebaseResponse response = _client.Get("RoomTypes/" + id);
                dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

                if (data == null)
                {
                    return NotFound();
                }

                // Chuyển dữ liệu thành đối tượng RoomType
                var curentRoom = JsonConvert.DeserializeObject<RoomType>(JsonConvert.SerializeObject(data));
                var currentImage = curentRoom.ImageUrl;

                // Kiểm tra nếu có ảnh mới được chọn
                if (ImageUrl != null && ImageUrl.Length > 0)
                {
                    // Kiểm tra loại file (chỉ cho phép ảnh)
                    var allowedExtensions = new[] { ".jpg", ".jpeg", ".png" };
                    var extension = Path.GetExtension(ImageUrl.FileName).ToLower();
                    if (!allowedExtensions.Contains(extension))
                    {
                        ModelState.AddModelError(string.Empty, "Only image files (.jpg, .jpeg, .png) are allowed.");
                        return View(roomType);
                    }

                    
                    await DeleteImageFromFirebaseStorage(currentImage);
                    

                    // Đẩy ảnh mới lên Firebase Storage và lấy URL
                    var newImageUrl = await UploadImageToFirebaseStorage(ImageUrl);
                    roomType.ImageUrl = newImageUrl;  // Gán lại ảnh mới
                }
                else
                {
                    roomType.ImageUrl = currentImage;
                }

                // Cập nhật lại thông tin loại phòng trong Firebase
                response = _client.Set("RoomTypes/" + id, roomType);

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

            return View(roomType);
        }

        [Route("/Admin/RoomType/Delete/{id}")]
        [HttpPost]
        public async Task<ActionResult> Delete(string id)
        {
            try
            {
                FirebaseResponse response = _client.Get("RoomTypes/" + id);
                dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

                if (data == null)
                {
                    return Json(new {error = true, message = "Không tìm thấy loại phòng!"});
                }

                // Chuyển dữ liệu thành đối tượng RoomType
                var curentRoom = JsonConvert.DeserializeObject<RoomType>(JsonConvert.SerializeObject(data));
                var currentImage = curentRoom.ImageUrl;

                await DeleteImageFromFirebaseStorage(currentImage);

                // Xóa loại phòng khỏi Firebase
                response = _client.Delete("RoomTypes/" + id);

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
                var task = storage.Child("RoomImages") // Lưu ảnh vào thư mục "RoomImages"
                                  .Child(fileName)
                                  .PutAsync(file.OpenReadStream());

                // Đợi quá trình tải lên hoàn tất
                await task;

                // Lấy URL tải được của ảnh
                var imageUrl = await storage.Child("RoomImages")
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
                await storage.Child("RoomImages").Child(fileName).DeleteAsync();
            }
            catch (Exception ex)
            {
                // Xử lý lỗi nếu không thể xóa ảnh
                throw new Exception("Error deleting image from Firebase: " + ex.Message);
            }
        }
    }
}
