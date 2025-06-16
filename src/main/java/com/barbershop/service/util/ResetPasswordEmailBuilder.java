package com.barbershop.service.util;

import com.barbershop.model.Customer;
import com.barbershop.model.Email;

public class ResetPasswordEmailBuilder {

    public static Email buildResetPasswordEmail(Customer user, String resetLink) {

        String subject = "Reset password for BarberApp";

        String body = String.format(
                """
                                       Hello %s,
                                       
                                       We received a request to reset your password. Please click the link below to set a new password:
                                       
                                       %s
                                       
                                       This link will expire in 1 hour. If you didn’t request this, you can safely ignore this email.
                                       
                                       Thank you!
                                       BarberApp Team
                                       """,
                user.getName(), resetLink
        );

        return new Email(user.getEmail(), subject, body);
    }

    public static Email buildPasswordResetSuccessEmail(Customer user) {
        String subject = "Your Password Was Reset – BarberApp";

        String body = String.format(
                """
                        Hello %s,
                        
                        This is a confirmation that your password was successfully reset. If you did not perform this action, please contact our support immediately.
                        
                        Thank you!
                        BarberApp Team
                        """,
                user.getName()
        );

        return new Email(user.getEmail(), subject, body);
    }
}
