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
                width: "10%",
                render: function (data, type, row) {
                    // Kiểm tra nếu dữ liệu là một đối tượng Date
                    if (data) {
                        var date = new Date(data);
                        var day = String(date.getDate()).padStart(2, '0');
                        var month = String(date.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0
                        var year = date.getFullYear();
                        return day + '/' + month + '/' + year;
                    }
                    return data; // Trả lại dữ liệu gốc nếu không phải ngày hợp lệ
                }
            },
            {
                data: 'CheckOut',
                width: "10%",
                render: function (data, type, row) {
                    // Kiểm tra nếu dữ liệu là một đối tượng Date
                    if (data) {
                        var date = new Date(data);
                        var day = String(date.getDate()).padStart(2, '0');
                        var month = String(date.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0
                        var year = date.getFullYear();
                        return day + '/' + month + '/' + year;
                    }
                    return data; // Trả lại dữ liệu gốc nếu không phải ngày hợp lệ
                }
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