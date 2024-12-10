using FireSharp.Interfaces;
using FireSharp.Response;
using HotelManage_LTDD.Models;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;

namespace HotelManage_LTDD.Areas.Admin.Controllers
{
    [Area("Admin")]
    public class BookingController : Controller
    {

        private readonly IFirebaseClient _client;

        public BookingController(IFirebaseClient client)
        {
            _client = client;
        }
        [Route("/Admin/Booking")]
        public ActionResult Index()
        {
            return View();
        }

        [Route("/Admin/Booking/Create")]
        public ActionResult Create()
        {
            return View();
        }

        [Route("/Admin/Booking/List")]
        public async Task<IActionResult> getList()
        {
            FirebaseResponse response = _client.Get("Bookings");
            return Content(response.Body, "application/json");
        }

        [Route("/Admin/Booking/Details/{id}")]
        public ActionResult Details(string id)
        {
            // Lấy dữ liệu từ Firebase theo ID
            FirebaseResponse response = _client.Get("Bookings/" + id);
            dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

            if (data == null)
            {
                return NotFound();

            }
            var booking = JsonConvert.DeserializeObject<Booking>(JsonConvert.SerializeObject(data));
            return View(booking);
        }

        [Route("/Admin/Booking/Create")]
        [HttpPost]
        public async Task<ActionResult> Create(Booking model)
        {
            if (ModelState.IsValid)
            {
                FirebaseResponse roomresponse = _client.Get("Rooms/" + model.RoomID);
                dynamic roomdata = JsonConvert.DeserializeObject<dynamic>(roomresponse.Body);

                if (roomdata == null)
                {
                    return NotFound();

                }
                var room = JsonConvert.DeserializeObject<Room>(JsonConvert.SerializeObject(roomdata));

                FirebaseResponse userresponse = _client.Get("Users/" + model.UserID);
                dynamic userdata = JsonConvert.DeserializeObject<dynamic>(userresponse.Body);

                if (userdata == null)
                {
                    return NotFound();

                }
                var user = JsonConvert.DeserializeObject<User>(JsonConvert.SerializeObject(userdata));

                model.RoomCode = room.Code;
                model.UserName = user.Name;
                model.Price = room.Price * (decimal)(model.CheckOut - model.CheckIn).TotalDays;
                model.Total = room.Price * (decimal)(model.CheckOut - model.CheckIn).TotalDays;
                if (room.Discount != 0)
                {
                    model.Total = (room.Price - (room.Discount/100) * room.Price) * (decimal)(model.CheckOut - model.CheckIn).TotalDays;
                }

                if (model.VoucherCode != null)
                {
                    FirebaseResponse voucherresponse = _client.Get("Vouchers/" + model.RoomID);
                    dynamic voucherdata = JsonConvert.DeserializeObject<dynamic>(voucherresponse.Body);

                    if (voucherdata == null)
                    {
                        return NotFound();

                    }
                    var voucher = JsonConvert.DeserializeObject<Room>(JsonConvert.SerializeObject(voucherdata));
                    model.VoucherID = voucher.ID;
                    model.VoucherDiscount = voucher.Discount;
                    model.Total = (room.Price - (room.Discount / 100) * room.Price - (voucher.Discount / 100) * room.Price) * (decimal)(model.CheckOut - model.CheckIn).TotalDays;
                }
                model.CheckIn.ToString("dd/MM/yyyy HH:mm:ss");
                model.CheckIn.ToString("dd/MM/yyyy HH:mm:ss");
                var data = model;
                PushResponse response = _client.Push("Bookings/", data);
                data.Id = response.Result.name;
                SetResponse setResponse = _client.Set("Bookings/" + data.Id, data);

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

        [Route("/Admin/Booking/Edit/{id}")]
        public ActionResult Edit(string id)
        {
            FirebaseResponse response = _client.Get("Bookings/" + id);
            dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

            if (data == null)
            {
                return NotFound();
            }
            var booking = JsonConvert.DeserializeObject<Booking>(JsonConvert.SerializeObject(data));
            return View(booking);
        }
        [Route("/Admin/Booking/Edit/{id}")]
        [HttpPost]
        public async Task<ActionResult> Edit(string id, Booking model)
        {
            if (ModelState.IsValid)
            {
                FirebaseResponse response = _client.Get("Bookings/" + id);
                dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

                if (data == null)
                {
                    return NotFound();

                }
                var booking = JsonConvert.DeserializeObject<Booking>(JsonConvert.SerializeObject(data));

                booking.Status = model.Status;

                // Cập nhật lại thông tin loại phòng trong Firebase
                response = _client.Set("Bookings/" + id, booking);

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

            return View(model);
        }

        [Route("/Admin/Booking/Delete/{id}")]
        [HttpPost]
        public async Task<ActionResult> Delete(string id)
        {
            try
            {
                FirebaseResponse response = _client.Delete("Bookings/" + id);

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
    }
}
