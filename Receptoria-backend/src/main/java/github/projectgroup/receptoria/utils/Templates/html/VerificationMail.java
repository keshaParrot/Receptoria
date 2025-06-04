package github.projectgroup.receptoria.utils.Templates.html;

public class VerificationMail {
    public static String generateValidationResponsePage(boolean result) {
        String message;
        String color;

        if (result) {
            message = "Your email has been successfully confirmed ✅";
            color = "#4CAF50";
        } else  {
            message = "This link has expired ❌";
            color = "#f44336";
        }

        return """
        <html>
        <body style="font-family:Arial,sans-serif; background-color:#f4f4f4; padding:20px;">
            <div style="max-width:600px; margin:auto; background-color:#ffffff; padding:30px;
                        border-radius:10px; box-shadow:0 2px 10px rgba(0,0,0,0.1); text-align:center;">
                <h2 style="color:%s;">%s</h2>
                <p style="font-size:14px; color:#777777;">You can now close this window.</p>
            </div>
        </body>
        </html>
        """.formatted(color, message);
    }
}
