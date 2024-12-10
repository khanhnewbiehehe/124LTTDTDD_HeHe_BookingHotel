using Firebase.Storage;
using FireSharp.Interfaces;
using FireSharp.Response;
using HotelManage_LTDD.Models;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using System.Linq;

namespace HotelManage_LTDD.Areas.Admin.Controllers
{
    [Area("Admin")]
    public class RoomController : Controller
    {

        private readonly IFirebaseClient _client;

        public RoomController(IFirebaseClient client)
        {
            _client = client;
        }
        [Route("/Admin/Room")]
        public ActionResult Index()
        {
            return View();
        }

        [Route("/Admin/Room/Create")]
        public ActionResult Create()
        {
            return View();
        }

        [Route("/Admin/Room/List")]
        public async Task<IActionResult> getList()
        {
            FirebaseResponse response = _client.Get("Rooms");
            return Content(response.Body, "application/json");
        }

        [Route("/Admin/Room/List/{CheckIn}/{CheckOut}")]
        public async Task<IActionResult> GetListFree(DateTime CheckIn, DateTime CheckOut)
        {
            // Lấy danh sách Rooms từ Firebase
            FirebaseResponse roomResponse = await _client.GetAsync("Rooms");
            var roomDictionary = roomResponse.ResultAs<Dictionary<string, Room>>();
            var rooms = roomDictionary?.Values.ToList() ?? new List<Room>();

            // Lấy danh sách Bookings từ Firebase
            FirebaseResponse bookingResponse = await _client.GetAsync("Bookings");
            var bookingDictionary = bookingResponse.ResultAs<Dictionary<string, Booking>>();
            var bookings = bookingDictionary?.Values.ToList() ?? new List<Booking>();

            // Lọc các phòng đã đặt trong khoảng thời gian CheckIn - CheckOut
            var bookedRoomIds = bookings
                .Where(b =>
                    (b.CheckIn <= CheckOut && b.CheckOut >= CheckIn) && b.Status != "Đã hoàn thành" && b.Status != "Bị hủy") // Kiểm tra giao khoảng thời gian
                .Select(b => b.RoomCode) // Lấy RoomId
                .Distinct() // Tránh trùng lặp
                .ToList();

            // Loại trừ các phòng đã đặt
            var freeRooms = rooms.Where(r => !bookedRoomIds.Contains(r.Code) && r.Status == "Sẵn sàng").ToList();

            // Trả về JSON
            return Json(freeRooms);
        }



        [Route("/Admin/Room/Details/{id}")]
        public ActionResult Details(string id)
        {
            // Lấy dữ liệu từ Firebase theo ID
            FirebaseResponse response = _client.Get("Rooms/" + id);
            dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

            if (data == null)
            {
                return NotFound();

            }
            var room = JsonConvert.DeserializeObject<Room>(JsonConvert.SerializeObject(data));
            return View(room);
        }

        [Route("/Admin/Room/Create")]
        [HttpPost]
        public async Task<ActionResult> Create(Room room, List<IFormFile> images)
        {
            if (ModelState.IsValid)
            {
                var rooms = _client.Get("Rooms/").ResultAs<Dictionary<string, Room>>();
                if (rooms != null && rooms.Values.Any(r => r.Code == room.Code))
                {
                    ModelState.AddModelError(nameof(Room.Code), "The room code is already in use. Please choose a different code.");
                    return View(room);
                }
                var data = room;

                // Tạo một bản ghi mới trong Firebase Realtime Database
                PushResponse response = _client.Push("Rooms/", data);
                data.Id = response.Result.name;

                // Xử lý ảnh
                foreach (var image in images)
                {
                    if (image != null && image.Length > 0)
                    {
                        // Kiểm tra loại file (ví dụ chỉ cho phép file hình ảnh)
                        var allowedExtensions = new[] { ".jpg", ".jpeg", ".png" };
                        var extension = Path.GetExtension(image.FileName).ToLower();
                        if (!allowedExtensions.Contains(extension))
                        {
                            ModelState.AddModelError(string.Empty, "Only image files (.jpg, .jpeg, .png) are allowed.");
                            return View();
                        }

                        // Đẩy ảnh lên Firebase Storage và lấy URL của ảnh
                        var imageUrl = await UploadImageToFirebaseStorage(image, data.Id);
                        data.Images.Add(imageUrl);  // Thêm URL ảnh vào Room.Images
                    }
                }

                // Lưu dữ liệu phòng (bao gồm các URL ảnh) vào Firebase Realtime Database
                SetResponse setResponse = _client.Set("Rooms/" + data.Id, data);

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


        [Route("/Admin/Room/Edit/{id}")]
        // GET: RoomTypeController/Edit/5
        public ActionResult Edit(string id)
        {
            FirebaseResponse response = _client.Get("Rooms/" + id);
            dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

            if (data == null)
            {
                return NotFound();
            }

            // Chuyển dữ liệu thành đối tượng RoomType
            var room = JsonConvert.DeserializeObject<Room>(JsonConvert.SerializeObject(data));
            return View(room);
        }
        [Route("/Admin/Room/Edit/{id}")]
        [HttpPost]
        public async Task<ActionResult> Edit(string id, Room room, List<IFormFile> images = null)
        {
            if (ModelState.IsValid)
            {
                var rooms = _client.Get("Rooms/").ResultAs<Dictionary<string, Room>>();
                if (rooms != null && rooms.Values.Any(r => r.Code == room.Code))
                {
                    ModelState.AddModelError(nameof(Room.Code), "The room code is already in use. Please choose a different code.");
                    return View(room);
                }
                FirebaseResponse response = _client.Get("Rooms/" + id);
                dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

                if (data == null)
                {
                    return NotFound();
                }

                // Chuyển dữ liệu thành đối tượng RoomType
                var curentRoom = JsonConvert.DeserializeObject<Room>(JsonConvert.SerializeObject(data));
                var currentImage = curentRoom.Images;

                if (images != null)
                {
                    await DeleteImageFromFirebaseStorage(currentImage, id);
                    foreach (var image in images)
                    {
                        // Kiểm tra nếu có ảnh mới được chọn
                        if (image != null && image.Length > 0)
                        {
                            // Kiểm tra loại file (chỉ cho phép ảnh)
                            var allowedExtensions = new[] { ".jpg", ".jpeg", ".png" };
                            var extension = Path.GetExtension(image.FileName).ToLower();
                            if (!allowedExtensions.Contains(extension))
                            {
                                ModelState.AddModelError(string.Empty, "Only image files (.jpg, .jpeg, .png) are allowed.");
                                return View(room);
                            }

                            //Đẩy ảnh mới lên Firebase Storage và lấy URL
                            var newImageUrl = await UploadImageToFirebaseStorage(image, id);
                            room.Images.Add(newImageUrl);  // Gán lại ảnh mới
                        }
                    }
                }
                else
                {
                    room.Images = currentImage;
                }
                
                // Cập nhật lại thông tin loại phòng trong Firebase
                response = _client.Set("Rooms/" + id, room);

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

            return View(room);
        }

        [Route("/Admin/Room/Delete/{id}")]
        [HttpPost]
        public async Task<ActionResult> Delete(string id)
        {
            try
            {
                FirebaseResponse response = _client.Get("Rooms/" + id);
                dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

                if (data == null)
                {
                    return Json(new { error = true, message = "Không tìm thấy phòng!" });
                }

                // Chuyển dữ liệu thành đối tượng RoomType
                var curentRoom = JsonConvert.DeserializeObject<Room>(JsonConvert.SerializeObject(data));
                var currentImage = curentRoom.Images;

                await DeleteImageFromFirebaseStorage(currentImage, id);

                // Xóa loại phòng khỏi Firebase
                response = _client.Delete("Rooms/" + id);

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

        public async Task<string> UploadImageToFirebaseStorage(IFormFile file, string id)
        {
            try
            {
                // Thiết lập các thông số Firebase Storage
                var storage = new FirebaseStorage("ltddhaha.firebasestorage.app");

                // Tạo tên file duy nhất để tránh bị ghi đè
                var fileName = Guid.NewGuid().ToString() + Path.GetExtension(file.FileName);

                // Tải lên file
                var task = storage.Child("Rooms" + id) // Lưu ảnh vào thư mục "RoomImages"
                                  .Child(fileName)
                                  .PutAsync(file.OpenReadStream());

                // Đợi quá trình tải lên hoàn tất
                await task;

                // Lấy URL tải được của ảnh
                var imageUrl = await storage.Child("Rooms" + id)
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
        public async Task DeleteImageFromFirebaseStorage(List<string> imageUrl, string id)
        {
            var storage = new FirebaseStorage("ltddhaha.firebasestorage.app");

            if (imageUrl != null)
            {
                foreach (var url in imageUrl)
                {
                    try
                    {
                        int startIndex = url.IndexOf("%2F") + 3;  // Tính từ vị trí sau "%2F"
                        int endIndex = url.IndexOf("?", startIndex); // Tìm dấu hỏi chấm bắt đầu từ startIndex

                        // Trích xuất tên ảnh
                        string fileName = url.Substring(startIndex, endIndex - startIndex);

                        // Tạo instance FirebaseStorage và xóa ảnh
                        await storage.Child("Rooms" + id).Child(fileName).DeleteAsync();
                    }
                    catch (Exception ex)
                    {
                        // Xử lý lỗi nếu không thể xóa ảnh
                        throw new Exception("Error deleting image from Firebase: " + ex.Message);
                    }
                }

            }


        }
    }
}
