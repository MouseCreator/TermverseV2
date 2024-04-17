import {StudySetPropsList} from "@/ui/data/data";

export async function fetchAllSets(): Promise<StudySetPropsList> {
    try {
        const response = await fetch('http://localhost:8080/sets');
        const data = await response.json();
        return ({ list: data });
    } catch (error) {
        console.error('Error fetching data:', error);
    }
    return ({ list: Array.of()})
}