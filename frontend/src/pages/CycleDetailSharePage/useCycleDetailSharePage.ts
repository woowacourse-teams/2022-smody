import { AddToCanvasProps } from './type';
import { useGetCycleById } from 'apis';
import { useRef, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { parseTime } from 'utils';

const sizeX = 200;
const sizeY = 600;

const useCycleDetailSharePage = () => {
  const { cycleId } = useParams();
  const canvasRef = useRef<HTMLCanvasElement>(null);

  const { data: cycleDetailData } = useGetCycleById({ cycleId: Number(cycleId) });

  // 캔버스에 이미지 추가하기
  const addToCanvas = ({ image, dx, date, index }: AddToCanvasProps) => {
    const ctx = canvasRef.current!.getContext('2d')!;

    const img = new Image();
    img.src = image;

    return new Promise((resolve) => {
      img.onload = () => {
        const { width, height } = img;
        const minSize = Math.min(width, height);
        const ratio = sizeY / minSize;
        const sWidth = sizeX / ratio;
        const sHeight = sizeY / ratio;
        const sx = width / 2 - 100 / ratio;
        const sy = height / 2 - 300 / ratio;

        ctx.drawImage(img, sx, sy, sWidth, sHeight, dx, 0, sizeX, sizeY);

        resolve(true);
      };
    });
  };

  useEffect(() => {
    if (typeof cycleDetailData === 'undefined') {
      return undefined;
    }

    const { cycleDetails } = cycleDetailData.data;
    const wrapper = async () => {
      const ctx = canvasRef.current!.getContext('2d')!;
      ctx.textAlign = 'center';

      await Promise.all(
        cycleDetails.map(({ progressImage, progressTime }, index) => {
          const { month, date } = parseTime(progressTime);

          return addToCanvas({
            image: progressImage,
            dx: 200 * index,
            date: `${month}.${date}`,
            index,
          });
        }),
      );

      // 글씨 쓰는 로직
      ctx.fillStyle = 'rgba(0, 0, 0, 0.3)';
      ctx.fillRect(0, 0, 600, 600);

      ctx.fillStyle = 'white';
      ctx.font = '40px Do Hyeon sans-serif';
      ctx.fillText('미라클 모닝 챌린지', 300, 490);

      ctx.font = '25px Do Hyeon sans-serif';

      cycleDetails.map(({ progressTime }, index) => {
        const { month, date } = parseTime(progressTime);

        ctx.fillText(`Day ${index + 1}`, sizeX * index + sizeX / 2, 535);
        ctx.fillText(`${month}.${date}`, sizeX * index + sizeX / 2, 570);
      });

      ctx.fillText('Smody', 540, 40);
    };

    wrapper();
  }, [cycleDetailData]);

  const handleSaveClick = () => {
    if (canvasRef.current === null) {
      return;
    }

    const image = canvasRef.current.toDataURL();

    const link = document.createElement('a');
    link.href = image; //주소
    link.download = '파일이름'; //다운로드될 파일 이름
    link.click(); //링크 클릭(다운로드됨)
  };

  return {
    canvasRef,
    cycleDetailData,
    handleSaveClick,
  };
};

export default useCycleDetailSharePage;
