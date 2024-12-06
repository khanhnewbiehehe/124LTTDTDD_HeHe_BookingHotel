$(document).ready(function () {
    $('#RoomTypeTable').DataTable({
        ajax: {
            url: '/Admin/RoomType/List',
            type: 'GET',
            dataSrc: function (json) {
                // Firebase trả về object, chuyển thành array
                return Object.keys(json).map(key => json[key]);
            }
        },
        columns: [
            {
                data: null,
                render: (data, type, row, meta) => meta.row + 1,
                width: "10%"
            }, // Số thứ tự
            {
                data: 'Name',
                width: "20%"
            },
            {
                data: 'People',
                width: "10%"
            },
            {
                data: 'Description',
                width: "30%"
            },
            {
                data: 'Id',
                render: (data, type, row) => {
                    return `
                            <a href = "Admin/RoomType/Details/${data}" class="btn btn-info btn-sm">Xem</a>
                            <a href = "Admin/RoomType/Edit/${data}" class="btn btn-success btn-sm">Sửa</a>
                            <a onClick="Delete('Admin/RoomType/Delete/${data}')" class="btn btn-danger btn-sm">Xóa</a>
                        `;
                },
                width: "30%"
            }
        ]
    });
});

function Delete(url) {

    var currentPage = $('#RoomTypeTable').DataTable().page();

    $.ajax({
        url: url,
        type: "POST",
        success: function (response) {
            if (response.success) {
                toastr.success(response.message);
                var table = $('#RoomTypeTable').DataTable();
                table.ajax.reload(function () {
                    table.page(currentPage).draw('page');
                });
            } else {
                toastr.error(response.message);
            }
        },
        error: function (xhr, status, error) {
            toastr.error("Lỗi: " + error)
        }
    });
}