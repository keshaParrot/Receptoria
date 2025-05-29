package github.projectgroup.receptoria.utils.mailtemplates;

public class PasswordResetHtmlTemplatesImpl implements MailHtmlTemplates {

    @Override
    public String getSubject() {
        return "Password Reset Request";
    }

    @Override
    public String getResetBody(String code) {
        return """
        <html>
        <body style="font-family:Arial,sans-serif; background-color:#f4f4f4; padding:20px;">
            <div style="max-width:600px; margin:auto; background-color:#ffffff; padding:30px; border-radius:10px; box-shadow:0 2px 10px rgba(0,0,0,0.1);">
                <h2 style="color:#333333;">Reset Your Password</h2>
                <p style="font-size:16px; color:#555555;">
                    We received a request to reset the password for your account.
                </p>
                <p style="font-size:20px; color:#d32f2f; text-align:center; font-weight:bold;">
                    Reset Code: %s
                </p>
                <p style="font-size:14px; color:#999999;">
                    If you didn’t request this, you can ignore this email safely.
                </p>
            </div>
        </body>
        </html>
    """.formatted(code);
    }



}
