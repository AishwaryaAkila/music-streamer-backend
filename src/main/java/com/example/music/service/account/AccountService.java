package com.example.music.service.account;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.web.multipart.MultipartFile;

import com.example.music.dto.AccountCreateDto;
import com.example.music.dto.SongsDto;
import com.example.music.dto.VerifyCodeDto;
import com.example.music.model.Account;
import com.example.music.model.History;

public interface AccountService {

	public Account createMember(AccountCreateDto accountDto) throws Exception;
	
	public Account createAdmin(AccountCreateDto accountDto);
	
	Optional<Account> findByUsernameOrEmail(String username, String email);

	Optional<Account> findByEmail(String email);

	Optional<Account> findByUsername(String username);
	public List<Object[]> getLatest();
	public byte[] decompressBytes(byte[] data);
	public List<Object[]> getSong(Long id);
	
	public List<Object[]> getAllLatest();
	
	Optional<Account> findById(Long id);
	void saveSong(MultipartFile file,MultipartFile image,SongsDto songsDto) throws IOException;
	
	int verifyCode(VerifyCodeDto verifyCodeDto);
	Account loginMember(AccountCreateDto accountDto) throws Exception;
	int verifyPassCode(VerifyCodeDto verifyCodeDto) ;
	int changePassword(AccountCreateDto accountCreateDto);

	void sendemail(Account account) throws MessagingException;
	String getUser(String email);
	
	public List<Object[]> getFavourites(Long id);
	public List<Object[]> getCategory(String key);
	public int addRecent(History history,Long id);
	public List<Object[]> getRecent(Long id);
	public List<Object[]> playNext(Long id);
	public List<Object[]> Search(String key);
	public List<Object[]> getAllRecent(Long id);
	public List<Object[]> playPrev(Long id);
	public void getIdBySong(Long id1,Long id2);
	
	public void removeFav(Long user_id,Long song_id);
	public List<Object[]> getLanguage(String key);
	public List<Long> getFavo1(Long key1);
	
	
}
