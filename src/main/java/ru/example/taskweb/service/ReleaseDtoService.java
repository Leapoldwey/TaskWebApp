package ru.example.taskweb.service;

import ru.example.taskweb.dto.ReleaseDto;
import ru.example.taskweb.entity.Release;
import ru.example.taskweb.entity.User;

import java.security.Principal;
import java.util.Optional;

public interface ReleaseDtoService {
    void setReleaseDto(ReleaseDto releaseDto, Optional<Release> optionalRelease, Long releaseId);

    void setRelease(ReleaseDto releaseDto, Release release, User user);
}
