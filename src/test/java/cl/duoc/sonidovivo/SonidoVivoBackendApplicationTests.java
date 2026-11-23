package cl.duoc.sonidovivo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SonidoVivoBackendApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarProductosEsPublicoYRetornaOk() throws Exception {
        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk());
    }

    @Test
    void listarUsuariosSinTokenRetornaUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isUnauthorized());
    }
}
