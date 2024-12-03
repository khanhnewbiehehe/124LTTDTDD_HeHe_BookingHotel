using FireSharp.Config;
using FireSharp.Interfaces;
using System.IO;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddSingleton<IFirebaseClient>(sp =>
{
    return new FireSharp.FirebaseClient(new FirebaseConfig
    {
        AuthSecret = builder.Configuration["Firebase:AuthSecret"],
        BasePath = builder.Configuration["Firebase:BasePath"]
    });
});


// Add services to the container.
builder.Services.AddControllersWithViews();


var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();

app.UseRouting();

app.UseAuthorization();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=RoomType}/{action=Index}/{id?}");

app.Run();
