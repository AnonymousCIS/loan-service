package org.anonymous.loan.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RestController;

@Profile("ml")
@RestController
@RequiredArgsConstructor
public class TrainController {
}
