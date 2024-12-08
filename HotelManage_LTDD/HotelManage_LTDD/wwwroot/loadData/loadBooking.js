$(document).ready(function () {
    $('#BookingTable').DataTable({
        ajax: {
            url: '/Admin/Booking/List',
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
                data: 'RoomCode',
                width: "10%"
            },
            {
                data: 'UserName',
                width: "10%"
            },
            {
                data: 'CheckIn',
                width: "10%"
            },
            {
                data: 'CheckOut',
                width: "10%"
            },
            {
                data: 'Total',
                width: "10%"
            },
            {
                data: 'Status',
                width: "10%"
            },
            {
                data: 'Id',
                render: (data, type, row) => {
                    return `
                            <a href = "/Admin/Booking/Details/${data}" class="btn btn-info btn-sm">Xem</a>
                            <a href = "/Admin/Booking/Edit/${data}" class="btn btn-success btn-sm">Sửa</a>
                            <a onClick="DeleteBooking('/Admin/Booking/Delete/${data}')" class="btn btn-danger btn-sm">Xóa</a>
                        `;
                },
                width: "30%"
            }
        ]
    });
});

function DeleteBooking(url) {

    var currentPage = $('#BookingTable').DataTable().page();

    $.ajax({
        url: url,
        type: "POST",
        success: function (response) {
            if (response.success) {
                toastr.success(response.message);
                var table = $('#BookingTable').DataTable();
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