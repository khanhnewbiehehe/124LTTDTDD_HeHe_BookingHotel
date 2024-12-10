using Firebase.Storage;
using FireSharp.Interfaces;
using FireSharp.Response;
using HotelManage_LTDD.Models;
using HotelManage_LTDD.ViewModels;
using Microsoft.AspNetCore.Mvc;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace HotelManage_LTDD.Controllers
{
    public class AccountController : Controller
    {
        private readonly IFirebaseClient _client;

        public AccountController(IFirebaseClient client)
        {
            _client = client;
        }
        public ActionResult Index()
        {
            return View();
        }
        public async Task<IActionResult> Login()
        {
            return View();
        }
        [HttpPost]
        public async Task<IActionResult> Login(LoginVM login)
        {
            // Lấy dữ liệu từ Firebase
            FirebaseResponse response = _client.Get("Users");
            dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);
            var list = new List<User>();
            if (data != null)
            {
                foreach (var item in data)
                {
                    list.Add(JsonConvert.DeserializeObject<User>(((JProperty)item).Value.ToString()));
                }
            }

            // Kiểm tra thông tin đăng nhập
            foreach (var userEntry in list)
            {
                if (userEntry.Email == login.Email && userEntry.Password == login.Password)
                {
                    // Kiểm tra quyền Admin
                    if (userEntry.Role == "Admin")
                    {
                        return RedirectToAction("Dashboard", "Booking", new { area = "Admin" });
                    }
                    TempData["Error"] = "Bạn không có quyền hạn truy cập vào trang quản lý!";
                    return View();
                }
            }

            // Nếu không đúng thông tin đăng nhập, hiển thị thông báo lỗi
            TempData["Error"] = "Email hoặc mật khẩu không chính xác.";
            return View();
        }

        public async Task<IActionResult> Register()
        {
            return View();
        }

        [HttpPost]
        public async Task<IActionResult> Register(User user)
        {
            if (ModelState.IsValid)
            {
                var data = user;

                // Thêm một người dùng mới vào Firebase
                PushResponse response = _client.Push("Users/", data);
                data.Id = response.Result.name;

                // Lưu dữ liệu người dùng vào Firebase
                SetResponse setResponse = _client.Set("Users/" + data.Id, data);

                if (setResponse.StatusCode == System.Net.HttpStatusCode.OK)
                {
                    // Nếu thành công, hiển thị thông báo thành công
                    TempData["Success"] = "Tạo tài khoản thành công!";
                    return RedirectToAction("Login");  // Redirect đến trang đăng nhập sau khi tạo tài khoản thành công
                }
                else
                {
                    // Nếu thất bại, hiển thị thông báo lỗi
                    TempData["Error"] = "Tạo tài khoản thất bại. Vui lòng thử lại.";
                }
            }

            // Nếu Model không hợp lệ, hiển thị thông báo lỗi
            TempData["Error"] = "Thông tin không hợp lệ. Vui lòng kiểm tra lại.";
            return View();
        }

    }
}

