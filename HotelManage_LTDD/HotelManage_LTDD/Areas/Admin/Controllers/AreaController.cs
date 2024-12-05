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
    public class AreaController : Controller
    {

        private readonly IFirebaseClient _client;

        public AreaController(IFirebaseClient client)
        {
            _client = client;
        }
        [Route("/Admin/Area")]
        public ActionResult Index()
        {
            return View();
        }

        [Route("/Admin/Area/Create")]
        public ActionResult Create()
        {
            return View();
        }

        [Route("/Admin/Area/List")]
        public async Task<IActionResult> getList()
        {
            FirebaseResponse response = _client.Get("Areas");
            return Content(response.Body, "application/json");
        }

        [Route("/Admin/Area/Details/{id}")]
        public ActionResult Details(string id)
        {
            // Lấy dữ liệu từ Firebase theo ID
            FirebaseResponse response = _client.Get("Areas/" + id);
            dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

            if (data == null)
            {
                return NotFound();

            }
            var area = JsonConvert.DeserializeObject<HotelArea>(JsonConvert.SerializeObject(data));
            return View(area);
        }

        [Route("/Admin/Area/Create")]
        [HttpPost]
        public async Task<ActionResult> Create(HotelArea model)
        {
            if (ModelState.IsValid)
            {
                var data = model;
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

        [Route("/Admin/Area/Edit/{id}")]
        public ActionResult Edit(string id)
        {
            FirebaseResponse response = _client.Get("Areas/" + id);
            dynamic data = JsonConvert.DeserializeObject<dynamic>(response.Body);

            if (data == null)
            {
                return NotFound();
            }
            var area = JsonConvert.DeserializeObject<HotelArea>(JsonConvert.SerializeObject(data));
            return View(area);
        }
        [Route("/Admin/Area/Edit/{id}")]
        [HttpPost]
        public async Task<ActionResult> Edit(string id, HotelArea model)
        {
            if (ModelState.IsValid)
            {
                // Cập nhật lại thông tin loại phòng trong Firebase
                FirebaseResponse response = _client.Set("Areas/" + id, model);

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

        [Route("/Admin/Area/Delete/{id}")]
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
