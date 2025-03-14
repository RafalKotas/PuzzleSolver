import axios from "axios"
import { selectedNonogramDetails } from "../../store/data/nonogram"

const API_URL = "http://localhost:5000/api/nonogram"

const saveNonogramToFile = (fileName : string, snd : selectedNonogramDetails) => {
    
    var data = JSON.stringify({
        ...snd
    });

    console.log(API_URL + "/save")
      
    var saveConfig = {
        method: "post",
        url: API_URL + "/save",
        headers: { 
          "Content-Type": "application/json"
        },
        params: {
            fileName: fileName
        },
        data : data
    };

    return axios(saveConfig)
}

const getNonogramDetailsFromFilePath = (fileName : string) => {
    
    var getNonogramsConfig = {
        method: "get",
        url: "../../resources/Nonograms/" + fileName + ".json?nocache=${Date.now()}",
        headers: { 
          "Content-Type": "application/json"
        }
    };

    return axios(getNonogramsConfig)
}

const getNonogramsUsingFilters = (page : number, itemsOnPage : number,
    sources : string[], years : string[], months : string[],
    minDifficulty : number, maxDifficulty : number,
    minHeight : number, maxHeight : number,
    minWidth : number, maxWidth : number) => {
          
        var getNonogramsConfig = {
            method: "get",
            url: API_URL + "/getNonogramsUsingFilters",
            headers: { 
              "Content-Type": "application/json"
            },
            params: {
                page,
                itemsOnPage,
                sources,
                years,
                months,
                minDifficulty,
                maxDifficulty,
                minHeight,
                maxHeight,
                minWidth,
                maxWidth
            }
        };
    
        return axios(getNonogramsConfig)

}

const getNonogramFilters = () => {
    var getNonogramFiltersConfig = {
        method: "get",
        url: API_URL + "/getFilters",
        headers: { 
          "Content-Type": "application/json"
        }
    };

    return axios(getNonogramFiltersConfig)
}

const getNonogramsList = () => {
    var getNonogramsListConfig = {
        method: "get",
        url: API_URL + "/getNonogramsList",
        headers: { 
          "Content-Type": "application/json"
        }
    };

    return axios(getNonogramsListConfig)
}

const NonogramService = {
    getNonogramDetailsFromFilePath,
    saveNonogramToFile,
    getNonogramsUsingFilters,
    getNonogramFilters,
    getNonogramsList
}

export default NonogramService