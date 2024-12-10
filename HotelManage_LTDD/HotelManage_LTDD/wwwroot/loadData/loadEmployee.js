$(document).ready(function () {
    $('#EmployeeTable').DataTable({
        ajax: {
            url: '/Admin/Employee/List',
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
                data: 'name',
                width: "20%"
            },
            {
                data: 'email',
                width: "10%"
            },
            {
                data: 'phoneNumber',
                width: "30%"
            },
            {
                data: 'id',
                render: (data, type, row) => {
                    return `
                            <a href = "/Admin/Employee/Details/${data}" class="btn btn-info btn-sm">Xem</a>
                            <a href = "/Admin/Employee/Edit/${data}" class="btn btn-success btn-sm">Sửa</a>
                            <a onClick="DeleteEmployee('/Admin/Employee/Delete/${data}')" class="btn btn-danger btn-sm">Xóa</a>
                        `;
                },
                width: "30%"
            }
        ]
    });
});

function DeleteEmployee(url) {

    var currentPage = $('#EmployeeTable').DataTable().page();

    $.ajax({
        url: url,
        type: "POST",
        success: function (response) {
            if (response.success) {
                toastr.success(response.message);
                var table = $('#EmployeeTable').DataTable();
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