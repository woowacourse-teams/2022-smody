import { AddToCanvasProps } from './type';
import { useGetCycleById } from 'apis';
import SmodyIcon from 'assets/smody_icon.png';
import { useRef, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { parseTime } from 'utils';

import { MAX_CHALLENGE_NAME_LENGTH } from 'constants/domain';
import { emojiList } from 'constants/style';

const SHARE_IMG_WIDTH = 200;
const SHARE_IMG_HEIGHT = 600;

const useCycleDetailSharePage = () => {
  const { cycleId } = useParams();
  const canvasRef = useRef<HTMLCanvasElement>(null);

  const { data: cycleDetailData } = useGetCycleById({ cycleId: Number(cycleId) });

  // 캔버스에 이미지 추가하기
  const addToCanvas = ({ image, dx, date, index }: AddToCanvasProps) => {
    const ctx = canvasRef.current!.getContext('2d')!;

    const img = new Image();
    img.src = image;
    img.crossOrigin = 'Anonymous';

    return new Promise((resolve) => {
      img.onload = () => {
        const { width, height } = img;
        const minSize = Math.min(width, height);
        const ratio = SHARE_IMG_HEIGHT / minSize;
        const sWidth = SHARE_IMG_WIDTH / ratio;
        const sHeight = SHARE_IMG_HEIGHT / ratio;
        const sx = width / 2 - SHARE_IMG_WIDTH / 2 / ratio;
        const sy = height / 2 - SHARE_IMG_HEIGHT / 2 / ratio;

        ctx.drawImage(
          img,
          sx,
          sy,
          sWidth,
          sHeight,
          dx,
          0,
          SHARE_IMG_WIDTH,
          SHARE_IMG_HEIGHT,
        );

        resolve(true);
      };
    });
  };

  useEffect(() => {
    if (typeof cycleDetailData === 'undefined') {
      return undefined;
    }

    const { cycleDetails, emojiIndex, challengeName, successCount } =
      cycleDetailData.data;

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

      const ctxFont = new FontFace(
        'Do Hyeon',
        'url(https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_one@1.0/BMDOHYEON.woff)',
      );

      ctxFont.load().then(() => {
        // 글씨 쓰는 로직
        document.fonts.add(ctxFont);

        ctx.fillStyle = 'rgba(0, 0, 0, 0.3)';
        ctx.fillRect(0, 0, 600, 600);

        ctx.font = '100px Do Hyeon';
        ctx.fillStyle = 'white';

        if (challengeName.length <= MAX_CHALLENGE_NAME_LENGTH / 2) {
          ctx.fillText(emojiList[emojiIndex], 300, 400);

          ctx.font = '30px Do Hyeon';
          ctx.fillText(`${challengeName}`, 300, 450);
        } else {
          ctx.fillText(emojiList[emojiIndex], 300, 370);

          const halfLength = Number(challengeName.length / 2);
          ctx.font = '25px Do Hyeon';
          ctx.fillText(`${challengeName.substring(0, halfLength + 1)}`, 300, 420);
          ctx.fillText(`${challengeName.substring(halfLength)}`, 300, 450);
        }

        ctx.font = '20px Do Hyeon';
        ctx.fillStyle = 'rgba(255, 255, 255, 0.6)';
        ctx.fillText(`${successCount}회차`, 300, 484);

        ctx.font = '25px Do Hyeon';
        cycleDetails.map(({ progressTime }, index) => {
          const { month, date } = parseTime(progressTime);

          ctx.fillStyle = 'white';
          ctx.fillText(
            `Day ${index + 1}`,
            SHARE_IMG_WIDTH * index + SHARE_IMG_WIDTH / 2,
            535,
          );
          ctx.fillStyle = 'rgba(255, 255, 255, 0.6)';
          ctx.fillText(
            `${month}.${date}`,
            SHARE_IMG_WIDTH * index + SHARE_IMG_WIDTH / 2,
            570,
          );
        });

        const iconImage = new Image();
        iconImage.src = SmodyIcon;
        iconImage.onload = () => {
          const { width, height } = iconImage;
          const ratio = 1;
          ctx.drawImage(
            iconImage,
            0,
            0,
            width,
            height,
            530,
            10,
            width * ratio,
            height * ratio,
          );
        };
      });
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
