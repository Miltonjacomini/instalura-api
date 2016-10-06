package br.com.alura.instalura.controllers;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.instalura.daos.FotoDao;
import br.com.alura.instalura.daos.UsuarioDao;
import br.com.alura.instalura.dtos.outputs.ComentariosResponse;
import br.com.alura.instalura.dtos.outputs.FotoResponse;
import br.com.alura.instalura.dtos.outputs.LikerResponse;
import br.com.alura.instalura.mappers.FotosMapper;
import br.com.alura.instalura.models.Comentario;
import br.com.alura.instalura.models.Foto;
import br.com.alura.instalura.models.Usuario;

@RestController
@CrossOrigin
public class FotosController {

	@Autowired
	private FotoDao fotoDao;
	@Autowired
	private UsuarioDao usuarioDao;

	@GetMapping(value="/api/fotos/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
	public List<FotoResponse> busca(@PathVariable("id") Integer idUsuario) {
		List<Foto> fotos = fotoDao.buscaFotosDosAmigos(idUsuario);
		return FotosMapper.map(fotos);
	}
	
	@Transactional
	@PostMapping(value="/api/fotos/{idFoto}/like",produces=MediaType.APPLICATION_JSON_VALUE)
	public Set<LikerResponse> like(@PathVariable("idFoto") Integer id) {
		
		Foto foto = fotoDao.findOne(id);
		Usuario alberto = usuarioDao.findOne(1);
		foto.adicionaLikeDo(alberto);
		return LikerResponse.map(foto.getLikers());
	}
	
	@Transactional
	@PostMapping(value="/api/fotos/{idFoto}/comment", produces=MediaType.APPLICATION_JSON_VALUE)
	public List<ComentariosResponse> comment(@RequestParam Comentario comentario, @PathVariable("idFoto") Integer id){
		Foto foto = fotoDao.findOne(id);
		Usuario alberto = usuarioDao.findOne(1);
		comentario.setUsuario(alberto);
		foto.adicionaComentario(comentario);
		return ComentariosResponse.map(foto.getComentarios());
	}
}
