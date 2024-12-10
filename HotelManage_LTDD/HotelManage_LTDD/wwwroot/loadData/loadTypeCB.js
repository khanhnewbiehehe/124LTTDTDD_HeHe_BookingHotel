$(document).ready(function () {
    // Gửi yêu cầu AJAX để lấy dữ liệu loại phòng
    $.ajax({
        url: '/Admin/RoomType/List', // Địa chỉ API
        type: 'GET', // Phương thức GET
        dataType: 'json', // Dữ liệu trả về dạng JSON
        success: function (data) {
            // Duyệt qua danh sách dữ liệu và thêm vào select
            var typeNameSelect = $('#TypeName');

            // Xóa các option mặc định trước khi thêm mới
            typeNameSelect.empty();
            typeNameSelect.append('<option value="">Chọn loại phòng</option>'); // Thêm option mặc định

            // Duyệt qua dữ liệu và thêm các option vào select
            $.each(data, function (index, roomType) {
                typeNameSelect.append('<option value="' + roomType.Name + '">' + roomType.Name + '</option>');
            });
        },
        error: function (xhr, status, error) {
            console.error('Error fetching room types:', error);
        }
    });
});
