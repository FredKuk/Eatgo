package kr.co.fastcampus.eatgo.interfaces;

import kr.co.fastcampus.eatgo.application.RestaurantService;
import kr.co.fastcampus.eatgo.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private RestaurantService restaurantService;

    //컨트롤러에 객체 주입
    @MockBean
    private RestaurantRepository restaurantRepository;
//    @SpyBean(MenuItemRepository.class) MOCKBEAN을 이용한 단위테스트시, JPA로 사용중인 것은 MOCKBEAN을 해줘야함
    @MockBean
    private MenuItemRepository menuItemRepository;

    @Test
    public void list() throws Exception {
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(new Restaurant(1004L, "Bob zip", "Seoul"));
        given(restaurantService.getRestaurants()).willReturn(restaurants);


        mvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"id\":1004")
                ))
                .andExpect(content().string(
                        containsString("\"name\":\"Bob zip\"")
                ));
    }

    @Test
    public void detail() throws Exception {

        Restaurant restaurant1 = new Restaurant(1004L, "Bob zip", "Seoul");
        restaurant1.addMenuItem((new MenuItem("Kimchi")));

        given(restaurantService.getRestaurant(1004L)).willReturn(restaurant1);

        mvc.perform(get("/restaurants/1004"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"id\":1004")
                ))
                .andExpect(content().string(
                        containsString("\"name\":\"Bob zip\"")
                ))
                .andExpect(content().string(
                        containsString("Kimchi")
                ));
    }

    @Test
    public void create() throws Exception {
        mvc.perform(post("/restaurants"))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/restaurants/1234"))
                .andExpect(content().string("{}"));
    }

}