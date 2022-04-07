package dev.azonov.giftservice.service;

import dev.azonov.giftservice.entity.ChildEntity;
import dev.azonov.giftservice.repository.ChildRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChildServiceTest {

    @InjectMocks
    private ChildService childService;

    @Mock
    private ChildRepository childRepositoryMock;

    @Test
    void createOrReadSavesNewChildIfNotExists() {
        when(childRepositoryMock.findFirstByFirstNameAndSecondNameAndMiddleName(null, null, null))
                .thenReturn(null);

        ChildEntity child = new ChildEntity();
        childService.createOrRead(child);

        verify(childRepositoryMock, times(1)).save(child);
    }

    @Test
    void createOrReadUpdatesChildIdIfChildExists() {
        long id = 10L;
        ChildEntity child = new ChildEntity();
        child.setId(id);

        when(childRepositoryMock.findFirstByFirstNameAndSecondNameAndMiddleName(null, null, null))
                .thenReturn(child);

        ChildEntity childToCheck = new ChildEntity();

        childService.createOrRead(childToCheck);
        verify(childRepositoryMock, times(0)).save(any());
        assertEquals(id, childToCheck.getId());
    }
}