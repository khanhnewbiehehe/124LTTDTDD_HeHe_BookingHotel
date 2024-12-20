$(document).ready(function () {
    $('#clientBookingTable').DataTable({
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
                            <button class="btn btn-primary btn-sm select-client" data-id="${data}" data-name="${row.name}">Chọn</button>
                        `;
                },
                width: "30%"
            }
        ]
    });
    $('#clientBookingTable').on('click', '.select-client', function () {
        const clientId = $(this).data('id'); // Lấy mã khách hàng
        const clientName = $(this).data('name'); // Lấy tên khách hàng

        // Điền mã khách hàng vào input UserId
        $('#UserID').val(clientId);
        $('#UserName').val(clientName);
        // Hiển thị thông báo hoặc cập nhật thêm thông tin (nếu cần)
        alert(`Khách hàng được chọn: ${clientName}`);
    });
});
