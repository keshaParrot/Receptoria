package github.projectgroup.receptoria.utils.mailtemplates;

public interface MailHtmlTemplates {
    String getSubject();

    String getResetBody(String code);
}
