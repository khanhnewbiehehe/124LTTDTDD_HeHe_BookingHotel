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
    public class AreaController : Controller
    {

        private readonly IFirebaseClient _client;

        public AreaController(IFirebaseClient client)
        {
            _client = client;
        }
        public ActionResult Index()
        {
            return View();
        }

        [Route("/Area/Create")]
        public ActionResult Create()
        {
            return View();
        }

        [Route("/Area/List")]
        public async Task<IActionResult> getList()
        {
            FirebaseResponse response = _client.Get("Areas");
            return Content(response.Body, "application/json");
        }

        [Route("/Area/Details/{id}")]
        public ActionResult Details(string id)
        {
            // Lấy dữ liệu từ Firebase theo ID
            FirebaseResponse response = _client.Get("Areas/" + id);
            dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

            if (data == null)
            {
                return NotFound();

            }
            var area = JsonConvert.DeserializeObject<Area>(JsonConvert.SerializeObject(data));
            return View(area);
        }

        [Route("/Area/Create")]
        [HttpPost]
        public async Task<ActionResult> Create(Area area)
        {
            if (ModelState.IsValid)
            {
                var data = area;
                PushResponse response = _client.Push("Areas/", data);
                data.Id = response.Result.name;
                SetResponse setResponse = _client.Set("Areas/" + data.Id, data);

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

        [Route("/Area/Edit/{id}")]
        public ActionResult Edit(string id)
        {
            FirebaseResponse response = _client.Get("Areas/" + id);
            dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

            if (data == null)
            {
                return NotFound();
            }
            var area = JsonConvert.DeserializeObject<Area>(JsonConvert.SerializeObject(data));
            return View(area);
        }
        [Route("/Area/Edit/{id}")]
        [HttpPost]
        public async Task<ActionResult> Edit(string id, Area area)
        {
            if (ModelState.IsValid)
            {
                // Cập nhật lại thông tin loại phòng trong Firebase
                FirebaseResponse response = _client.Set("Areas/" + id, area);

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

            return View(area);
        }

        [Route("/Area/Delete/{id}")]
        [HttpPost]
        public async Task<ActionResult> Delete(string id)
        {
            try
            {
                FirebaseResponse response = _client.Delete("Areas/" + id);

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
