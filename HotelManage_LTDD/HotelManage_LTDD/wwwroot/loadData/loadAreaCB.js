$(document).ready(function () {
    // Gửi yêu cầu AJAX để lấy dữ liệu loại phòng
    $.ajax({
        url: '/Admin/Area/List', // Địa chỉ API
        type: 'GET', // Phương thức GET
        dataType: 'json', // Dữ liệu trả về dạng JSON
        success: function (data) {
            // Duyệt qua danh sách dữ liệu và thêm vào select
            var areaNameSelect = $('#AreaName');

            // Xóa các option mặc định trước khi thêm mới
            areaNameSelect.empty();
            areaNameSelect.append('<option value="">Chọn khu vực</option>'); // Thêm option mặc định

            // Duyệt qua dữ liệu và thêm các option vào select
            $.each(data, function (index, areaType) {
                areaNameSelect.append('<option value="' + areaType.Name + '">' + areaType.Name + '</option>');
            });
        },
        error: function (xhr, status, error) {
            console.error('Error fetching area types:', error);
        }
    });
});
