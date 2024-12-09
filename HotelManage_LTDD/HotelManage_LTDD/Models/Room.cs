using System.ComponentModel.DataAnnotations.Schema;

namespace HotelManage_LTDD.Models
{
    public class Room
    {
        public string? Id { get; set; }
        public string TypeName { get; set; }
        public string AreaName { get; set; }
        public int Code { get; set; }
        public decimal Price { get; set; }
        public decimal? Discount { get; set; }
        public string Status { get; set; }
        public ICollection<string>? Images { get; set; } = new List<string>();
    }
}
