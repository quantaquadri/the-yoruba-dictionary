package org.oruko.dictionary.web;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.oruko.dictionary.model.WordEntry;
import org.oruko.dictionary.model.exception.RepositoryAccessError;
import org.oruko.dictionary.model.repository.WordEntryRepository;

import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WordEntryServiceTest {

    @Mock
    WordEntryRepository wordEntryRepository;

    // System under test
    @InjectMocks
    private NameEntryService nameEntryService;

    WordEntry wordEntry;

    WordEntry oldEntry;

    @Before
    public void setUp() {
        wordEntry = mock(WordEntry.class);
    }

    @Test
    public void testInsertTakingCareOfDuplicates_no_duplicates() throws Exception {
        String testName = "Ajani";
        when(wordEntry.getName()).thenReturn(testName);
        when(wordEntryRepository.findByName(testName)).thenReturn(null);
        nameEntryService.insertTakingCareOfDuplicates(wordEntry);

        verify(wordEntryRepository).save(wordEntry);
        verify(wordEntryRepository).findByName(testName);
    }

    @Test(expected = RepositoryAccessError.class)
    public void testInsertTakingCareOfDuplicates_with_duplicates_and_name_not_in_variant() throws Exception {
        String testName = "Ajani";
        WordEntry wordEntryMock = mock(WordEntry.class);
        when(wordEntryMock.getVariants()).thenReturn(null);
        when(wordEntry.getName()).thenReturn(testName);
        when(wordEntryRepository.findAll()).thenReturn(Collections.singletonList(wordEntryMock));
        when(wordEntryRepository.findByName(testName)).thenReturn(wordEntry);
        nameEntryService.insertTakingCareOfDuplicates(wordEntry);

        verify(wordEntryRepository).findAll();
        verify(wordEntryRepository).findByName(testName);
        verifyZeroInteractions(wordEntryRepository);
    }

    @Test(expected = RepositoryAccessError.class)
    public void testInsertTakingCareOfDuplicates_with_duplicates_and_name_already_in_variant() throws Exception {
        String testName = "Ajani";
        WordEntry wordEntryMock = mock(WordEntry.class);
        when(wordEntry.getName()).thenReturn(testName);
        when(wordEntryRepository.findAll()).thenReturn(Collections.singletonList(wordEntryMock));
        when(wordEntryRepository.findByName(testName)).thenReturn(wordEntry);
        nameEntryService.insertTakingCareOfDuplicates(wordEntry);

        verifyZeroInteractions(wordEntryRepository);
    }


    @Test
    public void testSave() throws Exception {
        nameEntryService.saveName(wordEntry);
        verify(wordEntryRepository).save(wordEntry);
        verifyNoMoreInteractions(wordEntryRepository);
    }

    @Test
    public void testUpdate() throws Exception {
        WordEntry oldEntry = mock(WordEntry.class);
        when(oldEntry.getName()).thenReturn("old name");
        when(wordEntryRepository.findByName(anyString())).thenReturn(oldEntry);
        nameEntryService.updateName(oldEntry, wordEntry);
        verify(oldEntry).update(wordEntry);
    }

    @Test
    public void testFindAll() throws Exception {
        //TODO

    }

    @Test
    public void testDeleteAllAndDuplicates() throws Exception {
        nameEntryService.deleteAllAndDuplicates();
        verify(wordEntryRepository).deleteAll();
        verifyNoMoreInteractions(wordEntryRepository);
    }

    @Test
    public void testdeleteNameEntryAndDuplicates() {
        WordEntry testName = mock(WordEntry.class);
        when(wordEntryRepository.findByName("lagbaja")).thenReturn(testName);
        nameEntryService.deleteNameEntryAndDuplicates("lagbaja");
        verify(wordEntryRepository).delete(testName);
    }
}