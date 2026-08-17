using SixLabors.ImageSharp;
using SixLabors.ImageSharp.Processing;
using SixLabors.ImageSharp.Formats.Jpeg;

var builder = WebApplication.CreateBuilder(args);
var app = builder.Build();

// dynamic route which accepts filter name as URL parameter
app.MapPost("/filter/{filterName}", async (string filterName, IFormFile file) =>
{
    if (file == null || file.Length == 0)
        return Results.BadRequest("No image was found");

    try
    {
        // read input file
        using var stream = file.OpenReadStream();
        using var image = await Image.LoadAsync(stream);

        // apply the filter based on the URL parameter
        switch (filterName.ToLower())
        {
            case "grayscale":
                image.Mutate(x => x.Grayscale());
                break;
            case "sepia":
                image.Mutate(x => x.Sepia());
                break;
            case "invert":
                image.Mutate(x => x.Invert());
                break;
            default:
                return Results.BadRequest($"Filter '{filterName}' is not supported");
        }

        // save in memory for sending
        var outStream = new MemoryStream();
        await image.SaveAsync(outStream, new JpegEncoder());
        outStream.Position = 0;

        // send back the filtered image
        return Results.File(outStream, "image/jpeg", "result.jpg");
    }
    catch (UnknownImageFormatException)
    {
        return Results.BadRequest("The file is not a valid image");
    }
})
.DisableAntiforgery();

app.Run();