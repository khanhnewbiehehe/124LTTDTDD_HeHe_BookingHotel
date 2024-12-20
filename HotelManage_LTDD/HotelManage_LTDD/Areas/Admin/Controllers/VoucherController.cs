﻿using Firebase.Storage;
using FireSharp.Interfaces;
using FireSharp.Response;
using HotelManage_LTDD.Models;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;

namespace HotelManage_LTDD.Areas.Admin.Controllers
{
    [Area("Admin")]
    public class VoucherController : Controller
    {

        private readonly IFirebaseClient _client;

        public VoucherController(IFirebaseClient client)
        {
            _client = client;
        }
        [Route("/Admin/Voucher")]
        public ActionResult Index()
        {
            return View();
        }

        [Route("/Admin/Voucher/Create")]
        public ActionResult Create()
        {
            return View();
        }

        [Route("/Admin/Voucher/List")]
        public async Task<IActionResult> getList()
        {
            FirebaseResponse response = _client.Get("Vouchers");
            return Content(response.Body, "application/json");
        }

        [Route("/Admin/Voucher/Details/{id}")]
        public ActionResult Details(string id)
        {
            // Lấy dữ liệu từ Firebase theo ID
            FirebaseResponse response = _client.Get("Vouchers/" + id);
            dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

            if (data == null)
            {
                return NotFound();

            }
            var Voucher = JsonConvert.DeserializeObject<Voucher>(JsonConvert.SerializeObject(data));
            return View(Voucher);
        }

        [Route("/Admin/Voucher/Create")]
        // POST: RoomTypeController/Create
        [HttpPost]
        public async Task<ActionResult> Create(Voucher Voucher, IFormFile ImageUrl)
        {
            if (ModelState.IsValid)
            {
                var vouchers = _client.Get("Vouchers/").ResultAs<Dictionary<string, Voucher>>(); // Lấy danh sách hiện có
                if (vouchers != null && vouchers.Values.Any(v => v.Code.Equals(Voucher.Code, StringComparison.OrdinalIgnoreCase)))
                {
                    ModelState.AddModelError(nameof(Voucher.Code), "The voucher code is already in use. Please choose a different code.");
                    return View(Voucher);
                }
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
                    Voucher.ImageUrl = imageUrl;  // Gán URL ảnh vào thuộc tính của RoomType
                }

                var data = Voucher;
                PushResponse response = _client.Push("Vouchers/", data);
                data.Id = response.Result.name;
                SetResponse setResponse = _client.Set("Vouchers/" + data.Id, data);

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

        [Route("/Admin/Voucher/Edit/{id}")]
        // GET: RoomTypeController/Edit/5
        public ActionResult Edit(string id)
        {
            FirebaseResponse response = _client.Get("Vouchers/" + id);
            dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

            if (data == null)
            {
                return NotFound();
            }

            // Chuyển dữ liệu thành đối tượng RoomType
            var Voucher = JsonConvert.DeserializeObject<Voucher>(JsonConvert.SerializeObject(data));
            return View(Voucher);
        }
        [Route("/Admin/Voucher/Edit/{id}")]
        [HttpPost]
        public async Task<ActionResult> Edit(string id, Voucher Voucher, IFormFile ImageUrl = null)
        {
            if (ModelState.IsValid)
            {
               
                FirebaseResponse response = _client.Get("Vouchers/" + id);
                dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

                if (data == null)
                {
                    return NotFound();
                }

                // Chuyển dữ liệu thành đối tượng RoomType
                var curentVoucher = JsonConvert.DeserializeObject<Voucher>(JsonConvert.SerializeObject(data));
                var currentImage = curentVoucher.ImageUrl;

                // Kiểm tra nếu có ảnh mới được chọn
                if (ImageUrl != null && ImageUrl.Length > 0)
                {
                    // Kiểm tra loại file (chỉ cho phép ảnh)
                    var allowedExtensions = new[] { ".jpg", ".jpeg", ".png" };
                    var extension = Path.GetExtension(ImageUrl.FileName).ToLower();
                    if (!allowedExtensions.Contains(extension))
                    {
                        ModelState.AddModelError(string.Empty, "Only image files (.jpg, .jpeg, .png) are allowed.");
                        return View(Voucher);
                    }


                    await DeleteImageFromFirebaseStorage(currentImage);


                    // Đẩy ảnh mới lên Firebase Storage và lấy URL
                    var newImageUrl = await UploadImageToFirebaseStorage(ImageUrl);
                    Voucher.ImageUrl = newImageUrl;  // Gán lại ảnh mới
                }
                else
                {
                    Voucher.ImageUrl = currentImage;
                }

                // Cập nhật lại thông tin loại phòng trong Firebase
                response = _client.Set("Vouchers/" + id, Voucher);

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

            return View(Voucher);
        }

        [Route("/Admin/Voucher/Delete/{id}")]
        [HttpPost]
        public async Task<ActionResult> Delete(string id)
        {
            try
            {
                FirebaseResponse response = _client.Get("Vouchers/" + id);
                dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

                if (data == null)
                {
                    return Json(new { error = true, message = "Không tìm thấy loại phòng!" });
                }

                // Chuyển dữ liệu thành đối tượng RoomType
                var curentVoucher = JsonConvert.DeserializeObject<Voucher>(JsonConvert.SerializeObject(data));
                var currentImage = curentVoucher.ImageUrl;

                await DeleteImageFromFirebaseStorage(currentImage);

                // Xóa loại phòng khỏi Firebase
                response = _client.Delete("Vouchers/" + id);

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
                var task = storage.Child("VoucherImages") // Lưu ảnh vào thư mục "RoomImages"
                                  .Child(fileName)
                                  .PutAsync(file.OpenReadStream());

                // Đợi quá trình tải lên hoàn tất
                await task;

                // Lấy URL tải được của ảnh
                var imageUrl = await storage.Child("VoucherImages")
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
                await storage.Child("VoucherImages").Child(fileName).DeleteAsync();
            }
            catch (Exception ex)
            {
                // Xử lý lỗi nếu không thể xóa ảnh
                throw new Exception("Error deleting image from Firebase: " + ex.Message);
            }
        }
    }
}
