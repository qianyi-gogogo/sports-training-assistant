
package com.qianyi.sportstrainingassistant.controller;

import com.qianyi.sportstrainingassistant.dto.request.profile.BodyProfileRequest;
import com.qianyi.sportstrainingassistant.model.BodyProfile;
import com.qianyi.sportstrainingassistant.service.BodyProfileRequestMapper;
import com.qianyi.sportstrainingassistant.service.BodyProfileService;
import com.qianyi.sportstrainingassistant.dto.request.profile.StrengthProfilePatchRequest;
import com.qianyi.sportstrainingassistant.dto.request.profile.BodyProfilePatchRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import com.qianyi.sportstrainingassistant.dto.response.profile.BodyProfileHistoryResponse;
import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/profile")
public class BodyProfileController {

    private final BodyProfileService service;

    // ==================== Constructor ====================

    public BodyProfileController(
            BodyProfileService service
    ) {
        this.service = service;
    }

    // ==================== Create ====================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BodyProfile createProfile(
            @PathVariable Long userId,
            @Valid @RequestBody BodyProfileRequest request
    ) {

        BodyProfile profile =
                BodyProfileRequestMapper.toModel(request);

        return service.createProfile(userId, profile);
    }

    // ==================== Read ====================

    @GetMapping("/current")
    public BodyProfile getCurrentProfile(
            @PathVariable Long userId
    ) {

        return service.getCurrentProfile(userId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Body profile not found"
                        )
                );
    }

    // ==================== Partial Update ====================

    @PatchMapping
    public BodyProfile patchProfile(
            @PathVariable Long userId,
            @Valid @RequestBody BodyProfilePatchRequest request
    ) {
        return service.patchProfile(userId, request);
    }

    // ==================== PATCH Strength Profile ====================

    @PatchMapping("/strength")
    public BodyProfile patchStrengthProfile(
            @PathVariable Long userId,
            @Valid @RequestBody StrengthProfilePatchRequest request
    ) {
        return service.patchStrengthProfile(
                userId,
                request
        );
    }
    // ==================== Get Profile History ====================

    @GetMapping("/history")
    public List<BodyProfileHistoryResponse> getProfileHistory(
            @PathVariable Long userId
    ) {
        return service.getProfileHistory(userId);
    }

}
