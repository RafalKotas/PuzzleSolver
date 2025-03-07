interface inputSliderProps {
    label: string,
    propertyName: string,
    minValue: number,
    maxValue: number,
    initialValue: number,
    step: number,
    icon: string
}

export const slitherlinkSliders : inputSliderProps[] = [
    {
        label: "Difficulty",
        propertyName: "difficulty",
        minValue: 1.0,
        maxValue: 5.0,
        initialValue: 3.0,
        step: 0.1,
        icon: "LeaderboardIcon"
    },
    {
        label: "Height",
        propertyName: "height",
        minValue: 5,
        maxValue: 20,
        initialValue: 10,
        step: 1,
        icon: "HeightIcon"
    },
    {
        label: "Width",
        propertyName: "width",
        minValue: 5,
        maxValue: 20,
        initialValue: 10,
        step: 1,
        icon: "WidthFullIcon"
    }
]