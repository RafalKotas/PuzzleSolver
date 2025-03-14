import { propDetails } from "../../../../CommonComponents/StringPropTextField/types"
import { generateMonthsList, generateYearsList } from "../../../../utils/dataGenerator"

const yearDefaultValues = generateYearsList(2015)
const monthDefaultValues = generateMonthsList()

export const nonogramStringProps : propDetails[] = [
    {
        label: "source",
        minLength: 3,
        maxLength: 20,
        defaultValue: "logi",
        helperText: "Letters and numbers only (3 - 20)",
        regex: /[a-zA-Z1-9_]/,
        required: true,
        notProvidedValue: "",
        defaultValues: ["logi", "logiMix", "pazyl_pl", "katana"]
    },
    {
        label: "year",
        minLength: 4,
        maxLength: 4,
        defaultValue: new Date().getFullYear() + "",
        helperText: "/^(20[0-2][0-9])/",
        regex: /^(20[0-2][0-9])$/,
        required: false,
        notProvidedValue: "N/D",
        defaultValues: yearDefaultValues
    },
    {
        label: "month",
        minLength: 2,
        maxLength: 2,
        defaultValue: ("0" + (new Date().getMonth() + 1)).slice(-2),
        helperText: "/^(1[0-2]|[1-9])$/",
        regex: /^(0?[1-9]|1[012])$/,// /^(1[0-2]|[1-9])$/,
        required: false,
        notProvidedValue: "N/D",
        defaultValues: monthDefaultValues
    }
]