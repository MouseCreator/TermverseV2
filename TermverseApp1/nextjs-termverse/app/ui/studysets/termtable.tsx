import {Term} from "@/app/ui/data/Term";
import {notFound} from "next/navigation";

export interface TermsProps {
    terms: Array<Term>;
}

export function TermsTable(props: TermsProps) {
    const terms = props.terms
    const hasPicture = terms.some((term) => term.picture_url !== null);
    const hasHint = terms.some((term) => term.hint !== null);

    return (
        <table className="w-full border-collapse table-fixed">
            <thead>
            <tr>
                <th className="px-4 py-2 bg-gray-200 text-left">Term</th>
                <th className="px-4 py-2 bg-gray-200 text-left">Definition</th>
                {hasPicture && <th className="px-4 py-2 bg-gray-200">Picture</th>}
                {hasHint && <th className="px-4 py-2 bg-gray-200">Hint</th>}
            </tr>
            </thead>
            <tbody>
            {terms.map((term, index) => (
                <tr
                    key={term.id}
                    className={`${
                        index % 2 === 0 ? 'bg-white' : 'bg-gray-100'
                    } rounded-lg`}
                >
                    <td className="px-4 py-2">{term.term}</td>
                    <td className="px-4 py-2">{term.definition}</td>
                    {hasPicture && (
                        <td className="px-4 py-2 flex justify-center">
                            {term.picture_url ? (
                                <img
                                    src={term.picture_url}
                                    alt={term.term}
                                    className="w-32 h-32 object-cover rounded-lg "
                                />
                            ) : (
                                <div className="w-32 h-32"></div>
                            )}
                        </td>
                    )}
                    {hasHint && <td className="px-4 py-2">{term.hint || ''}</td>}
                </tr>
            ))}
            </tbody>
        </table>
    );
}
export async function getStudySetTermsFromServer(id: number): Promise<TermsProps> {
    console.log('Fetching Terms Data...');
    const termsT: { [id: number]: Array<Term>; } = {};
    termsT[123] = [
        {
            id: 1,
            term: 'Apple',
            definition: 'A round fruit with red or green skin and crisp flesh.',
            hint: null,
            picture_url: '/mock/Apple.png',
            order: 1,
        },
        {
            id: 2,
            term: 'Banana',
            definition: 'An elongated, curved fruit with a soft, sweet flesh inside a yellow peel.',
            hint: 'Monkeys love eating this fruit.',
            picture_url: '/mock/Banana.png',
            order: 2,
        },
        {
            id: 3,
            term: 'Car',
            definition: 'A four-wheeled vehicle used for transportation.',
            hint: 'It runs on fuel or electricity.',
            picture_url: null,
            order: 3,
        },
        {
            id: 4,
            term: 'Bicycle',
            definition: 'A two-wheeled vehicle propelled by pedals.',
            hint: 'It is a popular form of exercise and eco-friendly transportation.',
            picture_url: null,
            order: 4,
        },
    ];
    let terms = termsT[id];
    if (!terms) {
        notFound();
    }
    return { terms };
}