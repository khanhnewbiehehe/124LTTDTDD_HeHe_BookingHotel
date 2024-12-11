$(document).ready(function () {
    // Hàm lấy danh sách phòng trống
    function getFreeRooms() {
        // Lấy giá trị từ các input ngày nhận và ngày trả phòng
        const checkIn = $("#CheckIn").val();
        const checkOut = $("#CheckOut").val();

        // Kiểm tra nếu người dùng chưa chọn ngày
        if (!checkIn || !checkOut) {
            alert("Vui lòng chọn ngày nhận phòng và ngày trả phòng!");
            return;
        }

        // Xóa DataTable cũ nếu có
        if ($.fn.dataTable.isDataTable('#FreeRoomTable')) {
            $('#FreeRoomTable').DataTable().clear().destroy();
        }

        // Gọi API để lấy danh sách phòng trống
        $('#FreeRoomTable').DataTable({
            ajax: {
                url: '/Admin/Room/List/' + checkIn + '/' + checkOut,
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
                    data: 'code',
                    width: "10%"
                },
                {
                    data: 'typeName',
                    width: "10%"
                },
                {
                    data: 'areaName',
                    width: "10%"
                },
                {
                    data: 'price',
                    width: "10%"
                },
                {
                    data: 'status',
                    width: "10%"
                },
                {
                    data: 'discount',
                    width: "10%"
                },
                {
                    data: 'id',
                    render: (data, type, row) => {
                        return `
                           <a href="/Admin/Booking/Create/${data}/${checkIn}/${checkOut}" class="btn btn-success">Đặt phòng</a>
                        `;
                    },
                    width: "30%"
                }
            ]
        });
    }

    // Gọi hàm khi người dùng thay đổi ngày
    $("#CheckIn, #CheckOut").on("change", getFreeRooms);
});
