package com.projeto.negociaIF.controllers;

import com.projeto.negociaIF.dtos.create.CategoriaCreateDTO;
import com.projeto.negociaIF.dtos.response.CategoriaResponseDTO;
import com.projeto.negociaIF.dtos.response.FotoResponseDTO;
import com.projeto.negociaIF.dtos.response.ItemResponseDTO;
import com.projeto.negociaIF.dtos.update.CategoriaUpdateDTO;
import com.projeto.negociaIF.model.Categoria;
import com.projeto.negociaIF.services.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarCategoriaPorId(@PathVariable Long id){
        Categoria categoria =  categoriaService.buscarCategoriaPorId(id);

        CategoriaResponseDTO categoriaResponseDTO = new CategoriaResponseDTO();
        BeanUtils.copyProperties(categoria,categoriaResponseDTO);

        return ResponseEntity.ok(categoriaResponseDTO);
    }

    @PostMapping()
    public ResponseEntity<CategoriaResponseDTO> criarCategoria(@RequestBody @Valid CategoriaCreateDTO categoriaDTO){
        Categoria categoria = new Categoria();
        BeanUtils.copyProperties(categoriaDTO,categoria);

        Categoria categoriaSalva = categoriaService.criarCategoria(categoria);

        CategoriaResponseDTO categoriaResponseDTO = new CategoriaResponseDTO();
        BeanUtils.copyProperties(categoriaSalva, categoriaResponseDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaResponseDTO);
    }

    @GetMapping()
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias(){
        List<Categoria> categorias = categoriaService.listarCategorias();

        List<CategoriaResponseDTO> lista = categorias.stream().map(cat -> {
            CategoriaResponseDTO categoriaResponseDTO = new CategoriaResponseDTO();
            BeanUtils.copyProperties(cat,categoriaResponseDTO);

            if(cat.getItens() != null){
                List<ItemResponseDTO> itensDTO = cat.getItens().stream().map(item -> {
                    ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
                    BeanUtils.copyProperties(item,itemResponseDTO);
                    itemResponseDTO.setCategoria(item.getCategoria().getNome());

                    if(item.getFotos() != null){
                        List<FotoResponseDTO> fotosDTO = item.getFotos().stream().map(foto ->
                                new FotoResponseDTO(foto.getId(), foto.getUrl())).toList();
                        itemResponseDTO.setFotos(fotosDTO);
                    }
                    return itemResponseDTO;
                }).toList();
                categoriaResponseDTO.setItens(itensDTO);
            }
            return categoriaResponseDTO;
        }).toList();

        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizarCategoria(@PathVariable Long id, @RequestBody @Valid CategoriaUpdateDTO categoriaDTO){
        Categoria categoriaAtualizada = new Categoria();
        BeanUtils.copyProperties(categoriaDTO,categoriaAtualizada);

        Categoria categoriaSalva =  categoriaService.atualizarCategoria(id, categoriaAtualizada);

        CategoriaResponseDTO categoriaResponseDTO = new CategoriaResponseDTO();
        BeanUtils.copyProperties(categoriaSalva,categoriaResponseDTO);

        return ResponseEntity.ok(categoriaResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCategoria(@PathVariable Long id){
        categoriaService.excluirCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
