using SixLabors.ImageSharp;
using SixLabors.ImageSharp.Processing;
using SixLabors.ImageSharp.Formats.Jpeg;

var builder = WebApplication.CreateBuilder(args);
var app = builder.Build();

app.MapPost("/grayscale", async (IFormFile file) =>
{
    if (file == null || file.Length == 0)
        return Results.BadRequest("No image was found");

    try
    {
        // Read Input File
        using var stream = file.OpenReadStream();
        using var image = await Image.LoadAsync(stream);

        // Apply the filter to the image
        image.Mutate(x => x.Grayscale());

        // Save in Ram memory for sending
        var outStream = new MemoryStream();
        await image.SaveAsync(outStream, new JpegEncoder());
        outStream.Position = 0;

        // Send back the filtered image
        return Results.File(outStream, "image/jpeg", "result.jpg");
    }
    catch (UnknownImageFormatException)
    {
        return Results.BadRequest("The file is not a valid image");
    }
})
.DisableAntiforgery();

app.Run();