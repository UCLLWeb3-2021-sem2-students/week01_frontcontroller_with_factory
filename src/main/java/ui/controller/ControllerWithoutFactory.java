package ui.controller;

import domain.model.Country;
import domain.service.CountryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/ControllerWithoutFactory")
public class ControllerWithoutFactory extends HttpServlet {
    private CountryService service = new CountryService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }


    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String command = "home";
        if (request.getParameter("command") != null) {
            command = request.getParameter("command");
        }

        String destination;
        switch (command) {
            case "home":
                destination = home(request, response);
                break;
            case "overview":
                destination = overview(request, response);
                break;
            case "add":
                destination = add(request, response);
                break;
            case "search":
                destination = search(request, response);
                break;
            default:
                destination = home(request, response);
        }

        request.getRequestDispatcher(destination).forward(request, response);
    }

    private String search(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        Country country = service.getCountryWithName(name);
        if (country != null) {
            request.setAttribute("country", country);
            return "countrySearchResult.jsp";
        }
        return "countrySearch.jsp";
    }

    private String home(HttpServletRequest request, HttpServletResponse response) {
        return "index.html";
    }

    private String add(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Country country = new Country();

        List<String> result = new ArrayList<String>();
        getName(country, request, result);
        getCapital(country, request, result);
        getNumberOfInhabitants(country, request, result);
        getNumberOfVotes(country, request, result);

        String destination;
        if (result.size() > 0) {
            request.setAttribute("result", result);
            destination = "countryForm.jsp";
        } else {
            service.addCountry(country);
            destination = overview(request, response);
        }

        return destination;
    }

    private String overview(HttpServletRequest request,
                            HttpServletResponse response) throws ServletException, IOException {
        Country mostPopular = service.getMostPopularCountry();
        request.setAttribute("popular", mostPopular);
        List<Country> countries = service.getCountries();
        request.setAttribute("countries", countries);
        return "countryOverview.jsp";
    }

    private void getName(Country country, HttpServletRequest request, List<String> result) {
        String name = request.getParameter("name");
        request.setAttribute("namePreviousValue", name);
        try {
            country.setName(name);
            request.setAttribute("nameClass", "has-success");
        } catch (Exception exc) {
            request.setAttribute("nameClass", "has-error");
            result.add(exc.getMessage());
        }
    }

    private void getCapital(Country country, HttpServletRequest request, List<String> result) {
        String capital = request.getParameter("capital");
        request.setAttribute("capitalPreviousValue", capital);
        try {
            country.setCapital(capital);
            request.setAttribute("capitalClass", "has-success");
        } catch (Exception exc) {
            request.setAttribute("capitalClass", "has-error");
            result.add(exc.getMessage());
        }
    }

    private void getNumberOfVotes(Country country, HttpServletRequest request, List<String> result) {
        String votes = request.getParameter("votes");
        request.setAttribute("votesPreviousValue", votes);
        try {
            int numberOfVotes = Integer.parseInt(votes);
            country.setVotes(numberOfVotes);
            request.setAttribute("votesClass", "has-success");
        } catch (NumberFormatException exc) {
            request.setAttribute("votesClass", "has-error");
            result.add("Please enter a valid number of votes!");
        } catch (Exception exc) {
            request.setAttribute("votesClass", "has-error");
            result.add(exc.getMessage());
        }
    }

    private void getNumberOfInhabitants(Country country, HttpServletRequest request, List<String> result) {
        String inhabitants = request.getParameter("inhabitants");
        request.setAttribute("inhabitantsPreviousValue", inhabitants);
        try {
            int numberOfInhabitants = Integer.parseInt(inhabitants);
            country.setNumberInhabitants(numberOfInhabitants);
            request.setAttribute("inhabitantsClass", "has-success");
        } catch (NumberFormatException exc) {
            request.setAttribute("inhabitantsClass", "has-error");
            result.add("Please enter a valid number of inhabitants!");
        } catch (Exception exc) {
            request.setAttribute("inhabitantsClass", "has-error");
            result.add(exc.getMessage());
        }
    }

}
