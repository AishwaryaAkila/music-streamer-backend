package com.example.music.service.account;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.music.dao.account.AccountDao;
import com.example.music.dao.verify_account.VerifyAccountDao;
import com.example.music.dto.AccountCreateDto;
import com.example.music.dto.SongsDto;
import com.example.music.dto.VerifyCodeDto;
import com.example.music.mail.Mail;
import com.example.music.mail.MailService;
import com.example.music.model.Account;
import com.example.music.model.History;
import com.example.music.model.Role;
import com.example.music.model.Songs;
import com.example.music.model.VerifyAccount;
import com.example.music.repository.FavouritesRepository;
import com.example.music.repository.HistoryRepository;
import com.example.music.repository.SongsRepository;
import com.example.music.repository.VerifyAccountRepository;
import com.example.music.service.role.RoleService;
import com.example.music.util.RandomUtil;

import groovy.util.logging.Log;

@Service
public class AccountServiceImpl implements AccountService{

	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private VerifyAccountDao verifyAccountDao;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private SongsRepository songsRepository;
	
	@Autowired
	private FavouritesRepository favouritesRepository;
	@Autowired
	private HistoryRepository historyRepository;
	
	@Autowired
	private VerifyAccountRepository verifyaccountrep;
	
	@Override
	public Account loginMember(AccountCreateDto accountDto) throws Exception{
		
		try {
			Account check=accountDao.findByEmail(accountDto.getEmail()).get();
			
			if(passwordEncoder.matches(accountDto.getPassword(), check.getPassword())) {
				return check;
			}
			return null;
			
		}
		catch (Exception e) {
			
		}
		return null;
		
		
		
	}
	
	
	
	@Override
	public Account createMember(AccountCreateDto accountDto) throws MessagingException {
		
		String email = accountDto.getEmail();
		String username = accountDto.getUsername();
		String password = accountDto.getPassword();
		
		Account account = new Account();
		account.setEmail(email);
		account.setUsername(username);
		account.setPassword(passwordEncoder.encode(password));
		account.setActive(false);
		
		account.setRole(Long.parseLong("2"));
		
		
		sendemail(account);
		
		
		return accountDao.create(account);
	}
	@Override
	public String getUser(String email) {
		try {
			Account account=findByEmail(email).get();
			if(account.isActive()) {
				sendemail(account);				
				
				return account.getId().toString();
			}			
			
			return "-1";
			
		}
		catch(Exception NoSuchElementException) {}
		return "-1";
		
	}
	@Override
	public void sendemail(Account account) throws MessagingException {
		String token = RandomUtil.generateRandomStringNumber(6).toUpperCase();
		VerifyAccount verifyAccount = new VerifyAccount();
		verifyAccount.setAccount(account);
		verifyAccount.setCreatedDate(LocalDateTime.now());
		verifyAccount.setExpiredDataToken(3);
		verifyAccount.setToken(token);
		verifyAccountDao.create(verifyAccount);
		
		Map<String, Object> maps = new HashMap<>();
		maps.put("account", account);
		maps.put("token", token);

		Mail mail = new Mail();
		mail.setFrom("care.info.music@gmail.com");
		mail.setSubject("Registration");
		mail.setTo(account.getEmail());
		mail.setModel(maps);
		mailService.sendEmail(mail);
		
		
	}
	
	@Override
	public Account createAdmin(AccountCreateDto accountDto) {
		String email = accountDto.getEmail();
		String username = accountDto.getUsername();
		String password = accountDto.getPassword();
		
		Account account = new Account();
		account.setEmail(email);
		account.setUsername(username);
		account.setPassword(passwordEncoder.encode(password));
		account.setRole(Long.parseLong("2"));
//		if(roleService.findById(2l).isPresent()) {
//			Role role = roleService.findById(2l).get();
//			account.addRole(role);
//		} 
		
		return accountDao.create(account);
	}

	@Override
	public Optional<Account> findByUsernameOrEmail(String username, String email) {
		return accountDao.findByUsernameOrEmail(username, email);
	}

	@Override
	public Optional<Account> findByEmail(String email) {
		return accountDao.findByEmail(email);
	}
	

	@Override
	public Optional<Account> findByUsername(String username) {
		return accountDao.findByUsername(username);
	}

	@Override
	public Optional<Account> findById(Long id) {
		return accountDao.findById(id);
	}
	
	public int verifyCode(VerifyCodeDto verifyCodeDto) {
		VerifyAccount verifyAccount=verifyaccountrep.findToken(verifyCodeDto.getId());
		if(verifyCodeDto.getToken().equals(verifyAccount.getToken())) {
		
		if(LocalDateTime.now().isBefore(verifyAccount.getExpiredDataToken())) {
				Account account = verifyAccount.getAccount();
				account.setActive(true);
				accountDao.update(account);
				return 11;
			}
			return 8;
			
		}
		else  {
			return 12;
		}
		
		
		
	}
	
	public int verifyPassCode(VerifyCodeDto verifyCodeDto) {
		VerifyAccount verifyAccount=verifyaccountrep.findToken(verifyCodeDto.getId());
		if(verifyCodeDto.getToken().equals(verifyAccount.getToken())) {
		
		if(LocalDateTime.now().isBefore(verifyAccount.getExpiredDataToken())) {
				Account account = verifyAccount.getAccount();
				account.setActive(true);
				accountDao.update(account);
				return 11;
			}
			return 8;
			
		}
		else  {
			return 12;
		}
		
	}
	
	
	public int changePassword(AccountCreateDto accountCreateDto) {
		Account account=accountDao.findByEmail(accountCreateDto.getEmail()).get();
		account.setPassword(passwordEncoder.encode(accountCreateDto.getPassword()));
		accountDao.update(account);
		return 1;
		
	}
	public List<Object[]> getRecent(Long id){
		List<Object[]> result=historyRepository.getRecent(id);
		for(Object[] res:result) {
			res[2]=decompressBytes((byte[]) res[2]);
		}
		return result;
	}
	public List<Object[]> getAllRecent(Long id){
		List<Object[]> result=historyRepository.getAllRecent(id);
		for(Object[] res:result) {
			res[2]=decompressBytes((byte[]) res[2]);
		}
		return result;
	}
	
	public List<Object[]> getLatest(){
		List<Object[]> result=songsRepository.findlatest();
		
		for(Object[] res:result) {
			res[2]=decompressBytes((byte[]) res[2]);
		}
		return result;
	}
	
	public List<Object[]> getAllLatest(){
		List<Object[]> result=songsRepository.findAlllatest();
		for(Object[] res:result) {
			res[2]=decompressBytes((byte[]) res[2]);
		}
		return result;
	}
	
	public byte[] decompressBytes(byte[] data) {
		
		        Inflater inflater = new Inflater();
		
		        inflater.setInput(data);
		
		        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		
		        byte[] buffer = new byte[1024];
		
		        try {
		
		            while (!inflater.finished()) {
		
		                int count = inflater.inflate(buffer);
		
		                outputStream.write(buffer, 0, count);
		
		            }
		
		            outputStream.close();
		
		        } catch (IOException ioe) {
		
		        } catch (DataFormatException e) {
		
		        }
		
		        return outputStream.toByteArray();
		
		    }
	public static byte[] compressBytes(byte[] data) {
		
		        Deflater deflater = new Deflater();
		
		        deflater.setInput(data);

		        deflater.finish();
		
		        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		
		        byte[] buffer = new byte[1024];
		
		        while (!deflater.finished()) {
		
		            int count = deflater.deflate(buffer);
		
		            outputStream.write(buffer, 0, count);
		
		        }
		
		       try {
		
		            outputStream.close();
		
		        } catch (IOException e) {
		
		        }
		
		        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
		
		        return outputStream.toByteArray();
		
		    }
	
	public void saveSong(MultipartFile file,MultipartFile image,SongsDto songsDto) throws IOException {
		Songs song = new Songs();
		
		song.setaudio_file(compressBytes(file.getBytes()));
		song.setaudio_image(compressBytes(image.getBytes()));
		song.setartist(songsDto.getartist());
		song.setaudio_name(songsDto.getaudio_name());
		song.setmovie_name(songsDto.getmovie_name());
		song.setcategory(songsDto.getcategory());
		song.setlanguage(songsDto.getlanguage());
		
		songsRepository.save(song);
		
	}
	
	public List<Object[]> getSong(Long id) {
		List<Object[]> result=songsRepository.playSong(id);
		for(Object[] res:result) {
			res[1]=decompressBytes((byte[]) res[1]);
		}
		return result;
	}
	public List<Object[]> playNext(Long id){
		if(!songsRepository.existsById(id)) {
			id=songsRepository.findFirst();
		}
		
		List<Object[]> result=songsRepository.playnext(id);
		for(Object[] res:result) {
			res[2]=decompressBytes((byte[]) res[2]);
			res[3]=decompressBytes((byte[]) res[3]);
		}
		return result;
		
		
	}
	
	public List<Object[]> playPrev(Long id){
		if(!songsRepository.existsById(id)) {
			id=songsRepository.findLast();
		}
		
		List<Object[]> result=songsRepository.playnext(id);
		for(Object[] res:result) {
			res[2]=decompressBytes((byte[]) res[2]);
			res[3]=decompressBytes((byte[]) res[3]);
			
		}
		return result;
		
		
	}
	
	public List<Object[]> Search(String key){
		List<Object[]> result = songsRepository.searchSong(key);
		for(Object[] res:result) {
			res[2]=decompressBytes((byte[]) res[2]);
		}
		return result;
	}
	
	public List<Object[]> getFavourites(Long id){
		List<Object[]> result=favouritesRepository.findFavourites(id);
		for(Object[] res:result) {
			res[2]=decompressBytes((byte[]) res[2]);
		}
		return result;
	}
	
	public List<Object[]> getCategory(String key){
		List<Object[]> result = songsRepository.findCategory(key);
		for(Object[] res:result) {
			res[2]=decompressBytes((byte[]) res[2]);
		}
		return result;
	}
	public List<Object[]> getLanguage(String key){
		List<Object[]> result = songsRepository.findLanguage(key);
		for(Object[] res:result) {
			res[2]=decompressBytes((byte[]) res[2]);
		}
		return result;
	}
	
	@Transactional
	public int addRecent(History history,Long id) {
		int count= historyRepository.findCount(id);
		
		if(count<10) {
			historyRepository.save(history);
		}
		else {
			Long s=historyRepository.get_first(id);
			historyRepository.Remove(s);
			historyRepository.save(history);
		}
		
		return 1;
	}
	@Transactional
	public void getIdBySong(Long id1,Long id2) {
		System.out.println("check");
		try {
			System.out.println("found");
		 Long id=historyRepository.getIdBysong(id1, id2);
		 historyRepository.Remove(id);
		}
		catch (Exception NoSuchElementException) {
			System.out.println("Not found");
		}
		 
	}
	
	public List<Long> getFavo1(Long key1){
		
			return favouritesRepository.getFavo(key1);
			
		
	}
	@Transactional
	public void removeFav(Long user_id,Long song_id) {
		favouritesRepository.remove(user_id, song_id);
	}
	
	

}