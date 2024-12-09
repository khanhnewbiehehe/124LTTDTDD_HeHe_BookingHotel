$(document).ready(function () {
    $('#VoucherTable').DataTable({
        ajax: {
            url: '/Admin/Voucher/List',
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
                data: 'Code',
                width: "20%"
            },
            {
                data: 'Discount',
                width: "10%"
            },
            {
                data: 'Quantity',
                width: "10%"
            },
            {
                data: 'Id',
                render: (data, type, row) => {
                    return `
                            <a href = "/Admin/Voucher/Details/${data}" class="btn btn-info btn-sm">Xem</a>
                            <a href = "/Admin/Voucher/Edit/${data}" class="btn btn-success btn-sm">Sửa</a>
                            <a onClick="DeleteVoucher('/Admin/Voucher/Delete/${data}')" class="btn btn-danger btn-sm">Xóa</a>
                        `;
                },
                width: "30%"
            }
        ]
    });
});

function DeleteVoucher(url) {

    var currentPage = $('#VoucherTable').DataTable().page();

    $.ajax({
        url: url,
        type: "POST",
        success: function (response) {
            if (response.success) {
                toastr.success(response.message);
                var table = $('#VoucherTable').DataTable();
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