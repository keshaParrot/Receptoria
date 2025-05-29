package github.projectgroup.receptoria.utils.mailtemplates;

public class EmailConfirmationHtmlTemplatesImpl implements MailHtmlTemplates {
    @Override
    public String getSubject() {
        return "Email Confirmation";
    }

    @Override
    public String getResetBody(String code) {
        return """
        <html>
        <body style="font-family:Arial,sans-serif; background-color:#f4f4f4; padding:20px;">
            <div style="max-width:600px; margin:auto; background-color:#ffffff; padding:30px; border-radius:10px; box-shadow:0 2px 10px rgba(0,0,0,0.1);">
                <h2 style="color:#333333;">Welcome!</h2>
                <p style="font-size:16px; color:#555555;">
                    Thank you for registering. To confirm your email address, please click the button below:
                </p>
                <div style="text-align:center; margin:30px 0;">
                    <a href="%s" style="background-color:#4CAF50; color:white; padding:14px 25px; text-align:center; text-decoration:none; display:inline-block; border-radius:5px;">
                        Confirm Email
                    </a>
                </div>
                <p style="font-size:14px; color:#999999;">
                    If you did not create this account, you can safely ignore this message.
                </p>
            </div>
        </body>
        </html>
    """.formatted(code);
    }
}
