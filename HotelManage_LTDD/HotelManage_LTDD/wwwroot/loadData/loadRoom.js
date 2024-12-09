$(document).ready(function () {
    $('#RoomTable').DataTable({
        ajax: {
            url: '/Admin/Room/List',
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
                data: 'Code',
                width: "10%"
            },
            {
                data: 'TypeName',
                width: "10%"
            },
            {
                data: 'AreaName',
                width: "10%"
            },
            {
                data: 'Price',
                width: "10%"
            },
            {
                data: 'Status',
                width: "10%"
            },
            {
                data: 'Discount',
                width: "10%"
            },
            {
                data: 'Id',
                render: (data, type, row) => {
                    return `
                            <a href = "/Admin/Room/Details/${data}" class="btn btn-info btn-sm">Xem</a>
                            <a href = "/Admin/Room/Edit/${data}" class="btn btn-success btn-sm">Sửa</a>
                            <a onClick="DeleteRoom('/Admin/Room/Delete/${data}')" class="btn btn-danger btn-sm">Xóa</a>
                        `;
                },
                width: "30%"
            }
        ]
    });
});

function DeleteRoom(url) {

    var currentPage = $('#RoomTable').DataTable().page();

    $.ajax({
        url: url,
        type: "POST",
        success: function (response) {
            if (response.success) {
                toastr.success(response.message);
                var table = $('#RoomTable').DataTable();
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