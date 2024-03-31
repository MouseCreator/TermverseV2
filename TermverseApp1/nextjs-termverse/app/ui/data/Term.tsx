export interface Term {
    id: number;
    term: string;
    definition: string;
    hint: string | null;
    picture_url: string | null;
    order: number;
}