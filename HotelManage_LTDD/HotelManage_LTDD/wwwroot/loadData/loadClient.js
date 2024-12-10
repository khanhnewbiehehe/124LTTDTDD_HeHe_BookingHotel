$(document).ready(function () {
    $('#ClientTable').DataTable({
        ajax: {
            url: '/Admin/Client/List',
            type: 'GET',
            dataSrc: function (json) {
                // Firebase trả về object, chuyển thành array
                return Object.keys(json).map(key => json[key]);
            }
        },
        columns: [
            {
                data: 'id',
                width: "20%"
            }, 
            {
                data: 'name',
                width: "20%"
            },
            {
                data: 'email',
                width: "20%"
            },
            {
                data: 'phoneNumber',
                width: "10%"
            },
            {
                data: 'id',
                render: (data, type, row) => {
                    return `
                            <a href = "/Admin/Client/Details/${data}" class="btn btn-info btn-sm">Xem</a>
                            <a href = "/Admin/Client/Edit/${data}" class="btn btn-success btn-sm">Sửa</a>
                            <a onClick="DeleteClient('/Admin/Client/Delete/${data}')" class="btn btn-danger btn-sm">Xóa</a>
                        `;
                },
                width: "30%"
            }
        ]
    });
});

function DeleteClient(url) {

    var currentPage = $('#ClientTable').DataTable().page();

    $.ajax({
        url: url,
        type: "POST",
        success: function (response) {
            if (response.success) {
                toastr.success(response.message);
                var table = $('#ClientTable').DataTable();
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