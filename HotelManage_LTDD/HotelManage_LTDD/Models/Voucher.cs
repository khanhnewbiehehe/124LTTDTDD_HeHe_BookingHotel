namespace HotelManage_LTDD.Models
{
    public class Voucher
    {
        public string? Id { get; set; }
        public string? Name { get; set; }
        public string? Description { get; set; }
        public string? ImageUrl { get; set; }
        public int Quantity { get; set; }
        public string? Code { get; set; }
        public string Status { get; set; }
        public decimal? Discount { get; set; }
    }
}
