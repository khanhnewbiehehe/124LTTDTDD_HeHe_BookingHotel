﻿$(document).ready(function () {
    $('#AreaTable').DataTable({
        ajax: {
            url: '/Admin/Area/List',
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
                width: "60%"
            },
            {
                data: 'Id',
                render: (data, type, row) => {
                    return `
                            <a href = "/Admin/Area/Edit/${data}" class="btn btn-success btn-sm">Sửa</a>
                            <a onClick="DeleteArea('/Admin/Area/Delete/${data}')" class="btn btn-danger btn-sm">Xóa</a>
                        `;
                },
                width: "30%"
            }
        ]
    });
});

function DeleteArea(url) {

    var currentPage = $('#AreaTable').DataTable().page();

    $.ajax({
        url: url,
        type: "POST",
        success: function (response) {
            if (response.success) {
                toastr.success(response.message);
                var table = $('#AreaTable').DataTable();
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