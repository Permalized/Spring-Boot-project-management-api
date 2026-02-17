package com.projects.taskManager.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {


    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;




}
