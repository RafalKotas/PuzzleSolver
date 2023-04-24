import { propDetails } from "../../../../CommonComponents/StringPropTextField/types"

export const ArchitectStringProps : propDetails[] = [
    {
        label: "source",
        minLength: 3,
        maxLength: 20,
        defaultValue: "pazyl_pl",
        helperText: "Letters and numbers only (3 - 20)",
        regex: /[a-zA-Z1-9_]/,
        required: true,
        notProvidedValue: ""
    }
]