import React, {useState, useEffect} from 'react';
import Header from "./Header";
import ImageList from "./ImageList";
import styles from '../../assets/scss/component/gallery/Gallery.scss';

export default function Index() {
    const [imageList, setImageList] = useState([]);

    useEffect(() => {
        (async () => {
            try {
                const response = await fetch(`${process.env.API_URL}/api/gallery`, {
                    method: 'get',
                    headers: {
                        'Content-Type': 'application/json',
                        'Accept': 'application/json'
                    }
                });

                if (!response.ok) {
                    throw new Error(`${response.status} ${response.statusText}`);
                }

                const json = await response.json();
                if (json.result !== 'success') {
                    throw new Error(`${json.result} ${json.message}`);
                }

                setImageList(json.data);
            } catch (err) {
                console.error(err);
            }
        })();
    }, []);

    const addImage = async (comment, file) => {
        try {

            // 1. upload file image
            const formData = new FormData();
            formData.append('file', file);

            let response = await fetch(`${process.env.API_URL}/api/storage`, {
                method: 'post',
                headers: {'Accept': 'application/json'},
                body: formData
            });

            if (!response.ok) {
                throw `${response.status} ${response.statusText}`;
            }

            let json = await response.json();
            if (json.result !== 'success') {
                throw json.message;
            }

            // 2. insert gallery
            response = await fetch(`${process.env.API_URL}/api/gallery`, {
                method: 'post',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    comment,
                    imageUrl: json.data
                })
            });

            if (!response.ok) {
                throw `${response.status} ${response.statusText}`;
            }

            json = await response.json();
            if (json.result !== 'success') {
                throw json.message;
            }

            // 3. rendering(update)
            setImageList([json.data, ...imageList]);
        } catch (err) {
            console.error(err);
        }
    };

    const deleteImage = async (no) => {
        try {
            // Delete
            const response = await fetch(`${process.env.API_URL}/api/gallery/${no}`, {
                method: 'delete',
                headers: {
                    'Accept': 'application/json'
                },
                body: null
            });

            // fetch success?
            if (!response.ok) {
                throw `${response.status} ${response.statusText}`;
            }

            // API success?
            const json = await response.json();
            if (json.result !== 'success') {
                throw json.message;
            }

            // re-rendering(update)
            setImageList(imageList.filter((item) => item.no !== parseInt(json.data)));
        } catch (err) {
            console.error(err);
        }
    };

    return (
        <div className={styles.Gallery}>
            <Header addImage={addImage}/>
            <ImageList imageList={imageList} deleteImage={deleteImage}/>
        </div>
    )
}