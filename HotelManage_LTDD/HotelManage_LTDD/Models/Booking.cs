namespace HotelManage_LTDD.Models
{
    public class Booking
    {
        public string? Id { get; set; }
        public string RoomID { get; set; }
        public int? RoomCode { get; set; }
        public string? UserName { get; set; }
        public string UserID { get; set; }
        public string? VoucherID { get; set; }
        public decimal? VoucherDiscount { get; set; }
        public decimal? RoomDiscount { get; set; }
        public string? VoucherCode { get; set; }
        public DateTime CheckIn { get; set; }
        public DateTime CheckOut { get; set; }
        public string Status { get; set; }
        public decimal? Price { get; set; }
        public decimal? Total {  get; set; }
    }
}
