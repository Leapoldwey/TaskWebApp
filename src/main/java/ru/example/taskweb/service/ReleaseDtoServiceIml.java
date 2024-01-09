package ru.example.taskweb.service;

import org.springframework.stereotype.Service;
import ru.example.taskweb.dto.ReleaseDto;
import ru.example.taskweb.entity.Release;
import ru.example.taskweb.entity.User;

import java.util.Optional;
@Service
public class ReleaseDtoServiceIml implements ReleaseDtoService {
    @Override
    public void setReleaseDto(ReleaseDto releaseDto, Optional<Release> optionalRelease, Long releaseId) {
        releaseDto.setId(optionalRelease.get().getId());
        releaseDto.setTitle(optionalRelease.get().getTitle());
        releaseDto.setReleaseDate(optionalRelease.get().getReleaseDate());
        releaseDto.setDescription(optionalRelease.get().getDescription());
        releaseDto.setVersion(optionalRelease.get().getVersion());
        releaseDto.setTaskId(optionalRelease.get().getTask().getId());
    }
    @Override
    public void setRelease(ReleaseDto releaseDto, Release release, User user) {
        release.setTitle(releaseDto.getTitle());
        release.setReleaseDate(releaseDto.getReleaseDate());
        release.setDescription(releaseDto.getDescription());
        release.setVersion(releaseDto.getVersion());
        release.setUser(user);
    }

}
