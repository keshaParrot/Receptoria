package github.projectgroup.receptoria.utils.Templates.mail;

public interface MailHtmlTemplates {
    String getSubject();

    String getResetBody(String code);
}
