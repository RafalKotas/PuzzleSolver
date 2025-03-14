export const generateYearsList = (startYear: number = 2015): string[] => {
    const currentYear = new Date().getFullYear();
    return Array.from({ length: currentYear - startYear + 1 }, (_, i) => (startYear + i).toString());
};

export const generateMonthsList = (): string[] => {
    return Array.from({ length: 12 }, (_, i) => (i + 1).toString().padStart(2, "0"));
};