namespace HotelManage_LTDD.Models
{
    public class User
    {
        public string? Id { get; set; }
        public string Name { get; set; }
        public string Email { get; set; }
        public string PhoneNumber { get; set; }
        public string Password { get; set; }
        public string? AvatarUrl { get; set; }
        public string? Role { get; set; }
        public int? Vip {  get; set; }
    }
}
